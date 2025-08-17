package com.example.foodorder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="feedback")
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="order_id")
    private Orders order;

    private int rating; // 1..5
    private String comment;
    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public Orders getOrder(){ return order; } public void setOrder(Orders order){ this.order=order; }
    public int getRating(){ return rating; } public void setRating(int rating){ this.rating=rating; }
    public String getComment(){ return comment; } public void setComment(String comment){ this.comment=comment; }
    public LocalDateTime getCreatedAt(){ return createdAt; } public void setCreatedAt(LocalDateTime createdAt){ this.createdAt=createdAt; }
}
