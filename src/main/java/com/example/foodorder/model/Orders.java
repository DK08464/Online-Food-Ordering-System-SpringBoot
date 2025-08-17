package com.example.foodorder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Orders {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(optional=false)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable=false, name="total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public void addItem(OrderItem it){
        it.setOrder(this);
        items.add(it);
    }

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public User getUser(){ return user; } public void setUser(User user){ this.user=user; }
    public Restaurant getRestaurant(){ return restaurant; } public void setRestaurant(Restaurant restaurant){ this.restaurant=restaurant; }
    public OrderStatus getStatus(){ return status; } public void setStatus(OrderStatus status){ this.status=status; }
    public List<OrderItem> getItems(){ return items; } public void setItems(List<OrderItem> items){ this.items=items; }
    public BigDecimal getTotalAmount(){ return totalAmount; } public void setTotalAmount(BigDecimal totalAmount){ this.totalAmount=totalAmount; }
}
