package com.foodify.catalogservice.repository;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
