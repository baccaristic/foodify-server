package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_IdInAndPromotionActiveTrueAndAvailableTrue(List<Long> restaurantIds);

    List<MenuItem> findByRestaurant_IdAndAvailableTrue(Long restaurantId);
}
