package com.example.foodorder.controller;

import com.example.foodorder.model.Feedback;
import com.example.foodorder.model.OrderStatus;
import com.example.foodorder.model.Orders;
import com.example.foodorder.repository.FeedbackRepo;
import com.example.foodorder.repository.OrderRepo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackRepo repo; private final OrderRepo orders;
    public FeedbackController(FeedbackRepo r, OrderRepo o){ this.repo=r; this.orders=o; }

    @PostMapping
    public Feedback submit(@RequestParam Long orderId, @RequestParam int rating, @RequestParam(required=false) String comment){
        Orders o = orders.findById(orderId).orElseThrow();
        if(o.getStatus()!= OrderStatus.DELIVERED) throw new IllegalStateException("Feedback allowed only after delivery");
        Feedback f = new Feedback(); f.setOrder(o); f.setRating(rating); f.setComment(comment);
        return repo.save(f);
    }
}
