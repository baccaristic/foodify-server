package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantSpecialDayRepository extends JpaRepository<RestaurantSpecialDay, Long> {
    List<RestaurantSpecialDay> findByRestaurant_IdOrderByDateAsc(Long restaurantId);
    Optional<RestaurantSpecialDay> findByIdAndRestaurant_Id(Long id, Long restaurantId);
}
