package com.example.foodorder.model;

import jakarta.persistence.*;

@Entity
@Table(name="restaurants")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private boolean active = true;

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
    public String getAddress(){ return address; } public void setAddress(String address){ this.address=address; }
    public boolean isActive(){ return active; } public void setActive(boolean active){ this.active=active; }
}
