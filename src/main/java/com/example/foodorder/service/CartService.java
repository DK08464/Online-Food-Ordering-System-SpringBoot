package com.example.foodorder.service;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.OrderItem;
import com.example.foodorder.repository.MenuItemRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {
    private final Map<Long, List<OrderItem>> carts = new ConcurrentHashMap<>();
    private final MenuItemRepo menuRepo;

    public CartService(MenuItemRepo menuRepo){ this.menuRepo = menuRepo; }

    public synchronized void addItem(Long userId, Long menuItemId, int qty){
        MenuItem mi = menuRepo.findById(menuItemId).orElseThrow(() -> new NoSuchElementException("Menu item not found"));
        if(!mi.isAvailable()) throw new IllegalStateException("Item unavailable");
        OrderItem oi = new OrderItem(); oi.setMenuItem(mi); oi.setQuantity(qty); oi.setPriceEach(mi.getPrice());
        carts.computeIfAbsent(userId, k -> new ArrayList<>()).add(oi);
    }

    public List<OrderItem> getCart(Long userId){
        return new ArrayList<>(carts.getOrDefault(userId, Collections.emptyList()));
    }

    public void clear(Long userId){ carts.remove(userId); }
}
