package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findByRestaurant_IdOrderByNameAsc(Long restaurantId);

    Optional<MenuCategory> findByRestaurant_IdAndNameIgnoreCase(Long restaurantId, String name);
}
