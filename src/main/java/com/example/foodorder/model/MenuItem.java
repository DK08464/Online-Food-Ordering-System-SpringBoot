package com.example.foodorder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="menu_items")
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    private String name;
    private String description;
    @Column(nullable=false)
    private BigDecimal price;
    private boolean available = true;

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public Restaurant getRestaurant(){ return restaurant; } public void setRestaurant(Restaurant restaurant){ this.restaurant=restaurant; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
    public String getDescription(){ return description; } public void setDescription(String description){ this.description=description; }
    public BigDecimal getPrice(){ return price; } public void setPrice(BigDecimal price){ this.price=price; }
    public boolean isAvailable(){ return available; } public void setAvailable(boolean available){ this.available=available; }
}
