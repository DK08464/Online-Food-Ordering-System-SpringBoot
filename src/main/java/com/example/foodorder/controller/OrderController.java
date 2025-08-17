package com.example.foodorder.controller;

import com.example.foodorder.model.*;
import com.example.foodorder.repository.OrderRepo;
import com.example.foodorder.repository.UserRepo;
import com.example.foodorder.service.CartService;
import com.example.foodorder.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CartService cart; private final OrderService orders; private final UserRepo users; private final OrderRepo orderRepo;

    public OrderController(CartService c, OrderService o, UserRepo u, OrderRepo orderRepo){
        this.cart=c; this.orders=o; this.users=u; this.orderRepo=orderRepo;
    }

    @PostMapping("/place")
    public Orders place(@AuthenticationPrincipal UserDetails me, @RequestParam Long restaurantId){
        User u = users.findByEmail(me.getUsername()).orElseThrow();
        List<OrderItem> items = new ArrayList<>(cart.getCart(u.getId()));
        Orders o = orders.placeOrder(u, restaurantId, items);
        cart.clear(u.getId());
        return o;
    }

    @GetMapping("/{id}/status")
    public OrderStatus status(@PathVariable Long id){
        return orderRepo.findById(id).orElseThrow().getStatus();
    }

    @PostMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders update(@PathVariable Long id, @RequestParam OrderStatus status){
        return orders.updateStatus(id, status);
    }

    @GetMapping("/my")
    public Page<Orders> myOrders(@AuthenticationPrincipal UserDetails me,
                                 @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        Long userId = users.findByEmail(me.getUsername()).orElseThrow().getId();
        return orderRepo.findByUserId(userId, PageRequest.of(page,size));
    }
}
