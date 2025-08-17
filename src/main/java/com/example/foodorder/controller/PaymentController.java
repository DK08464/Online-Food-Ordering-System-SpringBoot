package com.example.foodorder.controller;

import com.example.foodorder.model.Payment;
import com.example.foodorder.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService pay;
    public PaymentController(PaymentService p){ this.pay=p; }

    @PostMapping("/initiate")
    public Payment initiate(@RequestParam Long orderId, @RequestParam String token){
        return pay.initiate(orderId, token);
    }
}
