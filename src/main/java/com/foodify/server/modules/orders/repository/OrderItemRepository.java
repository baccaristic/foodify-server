package com.foodify.server.modules.orders.repository;

import com.foodify.server.modules.orders.domain.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = {"menuItem", "menuItemExtras"})
    List<OrderItem> findByOrder_IdIn(Collection<Long> orderIds);
    
    @Query("""
            SELECT oi.menuItem.id as itemId, oi.menuItem.name as itemName, 
                   COUNT(oi) as orderCount, oi.menuItem.price as price
            FROM OrderItem oi
            JOIN oi.order o
            WHERE o.restaurant.id = :restaurantId
            AND o.status = 'DELIVERED'
            AND o.date >= :startDate AND o.date < :endDate
            GROUP BY oi.menuItem.id, oi.menuItem.name, oi.menuItem.price
            ORDER BY COUNT(oi) DESC
            """)
    List<Object[]> findTopSellingItemsByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("limit") int limit
    );
}
