package com.example.foodorder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false) @JoinColumn(name="order_id", unique = true)
    private Orders order;

    private String providerRef;
    private BigDecimal amount;
    private String status; // INITIATED/SUCCESS/FAILED

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public Orders getOrder(){ return order; } public void setOrder(Orders order){ this.order=order; }
    public String getProviderRef(){ return providerRef; } public void setProviderRef(String providerRef){ this.providerRef=providerRef; }
    public BigDecimal getAmount(){ return amount; } public void setAmount(BigDecimal amount){ this.amount=amount; }
    public String getStatus(){ return status; } public void setStatus(String status){ this.status=status; }
}
