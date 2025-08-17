package com.example.foodorder.service;

import com.example.foodorder.dao.AuditJdbcDao;
import com.example.foodorder.model.*;
import com.example.foodorder.repository.OrderRepo;
import com.example.foodorder.repository.RestaurantRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final RestaurantRepo restRepo;
    private final AuditJdbcDao audit;

    public OrderService(OrderRepo orderRepo, RestaurantRepo restRepo, AuditJdbcDao audit) {
        this.orderRepo = orderRepo; this.restRepo = restRepo; this.audit = audit;
    }

    @Transactional
    public Orders placeOrder(User user, Long restaurantId, List<OrderItem> items){
        Restaurant r = restRepo.findById(restaurantId).orElseThrow(() -> new NoSuchElementException("Restaurant not found"));
        if(items.isEmpty()) throw new IllegalArgumentException("Cart is empty");
        Orders o = new Orders(); o.setUser(user); o.setRestaurant(r); o.setStatus(OrderStatus.CREATED);
        BigDecimal total = BigDecimal.ZERO;
        for(OrderItem it: items){
            o.addItem(it);
            total = total.add(it.getPriceEach().multiply(BigDecimal.valueOf(it.getQuantity())));
        }
        o.setTotalAmount(total);
        Orders saved = orderRepo.save(o);
        audit.log("INFO","ORDER","Order created","orderId="+saved.getId());
        return saved;
    }

    @Transactional
    public Orders updateStatus(Long orderId, OrderStatus status){
        Orders o = orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
        if(status != null) o.setStatus(status);
        audit.log("INFO","ORDER","Status changed","orderId="+orderId+"->"+o.getStatus());
        return o;
    }
}
