package com.foodify.catalogservice.repository;

import com.foodify.catalogservice.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurant_IdInAndPromotionActiveTrue(List<Long> restaurantIds);
}
