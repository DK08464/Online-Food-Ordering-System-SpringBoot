package com.example.foodorder.service;

import com.example.foodorder.dao.AuditJdbcDao;
import com.example.foodorder.model.*;
import com.example.foodorder.repository.OrderRepo;
import com.example.foodorder.repository.PaymentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepo payRepo;
    private final OrderRepo orderRepo;
    private final AuditJdbcDao audit;
    protected ExecutorService exec = Executors.newFixedThreadPool(2);

    public PaymentService(PaymentRepo payRepo, OrderRepo orderRepo, AuditJdbcDao audit){
        this.payRepo = payRepo; this.orderRepo = orderRepo; this.audit = audit;
    }

    @Transactional
    public Payment initiate(Long orderId, String token){
        Orders o = orderRepo.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Payment p = payRepo.findByOrderId(orderId).orElseGet(() -> {
            Payment np = new Payment();
            np.setOrder(o);
            np.setAmount(o.getTotalAmount());
            np.setStatus("INITIATED");
            return np;
        });
        p.setProviderRef(UUID.randomUUID().toString());
        payRepo.save(p);
        log.info("Payment initiated for order {}", orderId);
        exec.submit(() -> process(p.getId(), token));
        return p;
    }

    @Transactional
    public void process(Long paymentId, String token){
        Payment p = payRepo.findById(paymentId).orElseThrow();
        boolean ok = charge(token, p.getAmount());
        p.setStatus(ok ? "SUCCESS" : "FAILED");
        payRepo.save(p);
        Orders order = orderRepo.findById(p.getOrder().getId()).orElseThrow();
        order.setStatus(ok ? OrderStatus.PAID : OrderStatus.CANCELLED);
        audit.log(ok? "INFO":"ERROR","PAYMENT","Payment "+p.getStatus(),"paymentId="+paymentId);
        log.info("Payment {} for order {}", p.getStatus(), order.getId());
    }

    // Mock gateway
    private boolean charge(String token, BigDecimal amount){
        return token != null && !token.isBlank() && amount.compareTo(BigDecimal.ONE) >= 0;
    }
}
