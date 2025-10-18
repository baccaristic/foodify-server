package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantOperatingHourRepository extends JpaRepository<RestaurantWeeklyOperatingHour, Long> {
    List<RestaurantWeeklyOperatingHour> findByRestaurant_IdOrderByDayOfWeekAsc(Long restaurantId);
}
