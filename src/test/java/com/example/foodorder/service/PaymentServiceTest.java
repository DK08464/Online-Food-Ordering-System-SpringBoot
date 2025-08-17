package com.example.foodorder.service;

import com.example.foodorder.dao.AuditJdbcDao;
import com.example.foodorder.model.*;
import com.example.foodorder.repository.OrderRepo;
import com.example.foodorder.repository.PaymentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock private PaymentRepo payRepo;
    @Mock private OrderRepo orderRepo;
    @Mock private AuditJdbcDao audit;
    @Mock private ExecutorService executor;

    @InjectMocks private PaymentService paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Injecting mock ExecutorService to avoid running real threads
        paymentService = new PaymentService(payRepo, orderRepo, audit) {
            {
                this.exec = executor;
            }
        };
    }

    @Test
    void initiate_createsNewPayment_whenNoneExists() {
        Orders order = new Orders();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("250.00"));

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(payRepo.findByOrderId(1L)).thenReturn(Optional.empty());
        when(payRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.initiate(1L, "sometoken");

        assertNotNull(result);
        assertEquals(order, result.getOrder());
        assertEquals(new BigDecimal("250.00"), result.getAmount());
        assertEquals("INITIATED", result.getStatus());
        assertNotNull(result.getProviderRef());

        verify(payRepo).save(result);
        verify(executor).submit(any(Runnable.class));
    }

    @Test
    void initiate_usesExistingPayment_ifPresent() {
        Orders order = new Orders();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("100.00"));

        Payment existing = new Payment();
        existing.setOrder(order);
        existing.setAmount(new BigDecimal("100.00"));
        existing.setStatus("INITIATED");

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(payRepo.findByOrderId(1L)).thenReturn(Optional.of(existing));
        when(payRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.initiate(1L, "sometoken");

        assertSame(existing, result);
        assertNotNull(result.getProviderRef());
        verify(payRepo).save(existing);
        verify(executor).submit(any(Runnable.class));
    }

    @Test
    void initiate_throwsException_whenOrderNotFound() {
        when(orderRepo.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initiate(999L, "token");
        });

        assertEquals("Order not found", ex.getMessage());
        verifyNoInteractions(payRepo);
        verifyNoInteractions(executor);
    }
}
