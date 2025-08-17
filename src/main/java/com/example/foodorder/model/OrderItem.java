package com.example.foodorder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="order_id")
    private Orders order;

    @ManyToOne(optional=false) @JoinColumn(name="menu_item_id")
    private MenuItem menuItem;

    private int quantity;
    private BigDecimal priceEach;

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public Orders getOrder(){ return order; } public void setOrder(Orders order){ this.order=order; }
    public MenuItem getMenuItem(){ return menuItem; } public void setMenuItem(MenuItem menuItem){ this.menuItem=menuItem; }
    public int getQuantity(){ return quantity; } public void setQuantity(int quantity){ this.quantity=quantity; }
    public BigDecimal getPriceEach(){ return priceEach; } public void setPriceEach(BigDecimal priceEach){ this.priceEach=priceEach; }
}
