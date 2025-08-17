package com.example.foodorder.controller;

import com.example.foodorder.model.OrderItem;
import com.example.foodorder.model.User;
import com.example.foodorder.repository.UserRepo;
import com.example.foodorder.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cart;
    private final UserRepo users;

    public CartController(CartService cart, UserRepo users){ this.cart=cart; this.users=users; }

    @PostMapping("/add")
    public void add(@AuthenticationPrincipal UserDetails me, @RequestParam Long itemId, @RequestParam int qty){
        Long userId = users.findByEmail(me.getUsername()).orElseThrow().getId();
        cart.addItem(userId, itemId, qty);
    }

    @GetMapping
    public List<OrderItem> view(@AuthenticationPrincipal UserDetails me){
        Long userId = users.findByEmail(me.getUsername()).orElseThrow().getId();
        return cart.getCart(userId);
    }

    @DeleteMapping
    public void clear(@AuthenticationPrincipal UserDetails me){
        Long userId = users.findByEmail(me.getUsername()).orElseThrow().getId();
        cart.clear(userId);
    }
}
