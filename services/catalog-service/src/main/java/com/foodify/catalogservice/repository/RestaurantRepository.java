package com.foodify.catalogservice.repository;

import com.foodify.catalogservice.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
