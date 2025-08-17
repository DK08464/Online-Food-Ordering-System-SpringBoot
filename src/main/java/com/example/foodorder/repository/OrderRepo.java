package com.example.foodorder.repository;

import com.example.foodorder.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    Page<Orders> findByUserId(Long userId, Pageable pageable);
}
