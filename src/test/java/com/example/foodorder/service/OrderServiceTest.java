package com.example.foodorder.service;

import com.example.foodorder.dao.AuditJdbcDao;
import com.example.foodorder.model.*;
import com.example.foodorder.repository.OrderRepo;
import com.example.foodorder.repository.RestaurantRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock OrderRepo orderRepo;
    @Mock RestaurantRepo restRepo;
    @Mock AuditJdbcDao audit;

    @InjectMocks OrderService svc;

    @Test
    void placeOrder_computesTotal_andPersists(){
        User u = new User(); u.setId(1L);
        Restaurant r = new Restaurant(); r.setId(10L);
        when(restRepo.findById(10L)).thenReturn(Optional.of(r));
        when(orderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        OrderItem oi = new OrderItem(); oi.setPriceEach(new BigDecimal("120.00")); oi.setQuantity(2);
        Orders o = svc.placeOrder(u, 10L, List.of(oi));
        assertEquals(new BigDecimal("240.00"), o.getTotalAmount());
        verify(audit).log(eq("INFO"), eq("ORDER"), anyString(), contains("orderId"));
    }
}
