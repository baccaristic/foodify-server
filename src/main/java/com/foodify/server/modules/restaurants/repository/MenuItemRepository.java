package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_IdInAndPromotionActiveTrueAndAvailableTrue(List<Long> restaurantIds);

    List<MenuItem> findByRestaurant_IdAndAvailableTrue(Long restaurantId);
    
    @Query("""
            SELECT mi FROM MenuItem mi
            LEFT JOIN FETCH mi.categories
            WHERE mi.restaurant.id = :restaurantId
            AND (:category IS NULL OR EXISTS (
                SELECT 1 FROM mi.categories c WHERE c.name = :category
            ))
            """)
    Page<MenuItem> findByRestaurantIdWithFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("category") String category,
            Pageable pageable
    );
}
