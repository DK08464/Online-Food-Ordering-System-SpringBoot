package com.example.foodorder.service;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.OrderItem;
import com.example.foodorder.repository.MenuItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private MenuItemRepo menuRepo;
    private CartService cartService;

    @BeforeEach
    void setup() {
        menuRepo = mock(MenuItemRepo.class);
        cartService = new CartService(menuRepo);
    }

    @Test
    void addItem_addsOrderItemToCart_whenItemAvailable() {
        Long userId = 1L;
        Long menuItemId = 100L;

        MenuItem mi = new MenuItem();
        mi.setId(menuItemId);
        mi.setPrice(new BigDecimal("10.50"));
        mi.setAvailable(true);

        when(menuRepo.findById(menuItemId)).thenReturn(java.util.Optional.of(mi));

        cartService.addItem(userId, menuItemId, 3);

        List<OrderItem> cart = cartService.getCart(userId);
        assertEquals(1, cart.size());
        OrderItem oi = cart.get(0);
        assertEquals(mi, oi.getMenuItem());
        assertEquals(3, oi.getQuantity());
        assertEquals(new BigDecimal("10.50"), oi.getPriceEach());
    }

    @Test
    void addItem_throwsNoSuchElementException_whenMenuItemNotFound() {
        when(menuRepo.findById(anyLong())).thenReturn(java.util.Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
            cartService.addItem(1L, 999L, 1);
        });
        assertEquals("Menu item not found", ex.getMessage());
    }

    @Test
    void addItem_throwsIllegalStateException_whenMenuItemUnavailable() {
        MenuItem mi = new MenuItem();
        mi.setAvailable(false);
        when(menuRepo.findById(anyLong())).thenReturn(java.util.Optional.of(mi));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            cartService.addItem(1L, 100L, 1);
        });
        assertEquals("Item unavailable", ex.getMessage());
    }

    @Test
    void getCart_returnsEmptyList_whenNoCartExists() {
        List<OrderItem> cart = cartService.getCart(123L);
        assertNotNull(cart);
        assertTrue(cart.isEmpty());
    }

    @Test
    void clear_removesCartForUser() {
        Long userId = 1L;
        MenuItem mi = new MenuItem();
        mi.setAvailable(true);
        mi.setPrice(new BigDecimal("5"));
        when(menuRepo.findById(anyLong())).thenReturn(java.util.Optional.of(mi));

        cartService.addItem(userId, 1L, 2);
        assertFalse(cartService.getCart(userId).isEmpty());

        cartService.clear(userId);
        assertTrue(cartService.getCart(userId).isEmpty());
    }
}
 