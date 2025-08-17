package com.example.foodorder.repository;

import com.example.foodorder.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> { }
