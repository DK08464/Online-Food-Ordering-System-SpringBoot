package com.example.foodorder.repository;

import com.example.foodorder.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByOrderRestaurantId(Long restId, Pageable pageable);
}
