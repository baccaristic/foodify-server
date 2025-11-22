package com.foodify.server.modules.admin.order.repository;

import com.foodify.server.modules.admin.order.dto.ClientOrderDetailDto;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AdminOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderTime >= :startDate AND o.orderTime < :endDate")
    long countOrdersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByStatus(OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.orderTime >= :startDate AND o.orderTime < :endDate")
    BigDecimal sumTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByClientId(Long clientId);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.client.id = :clientId")
    BigDecimal sumTotalByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COALESCE(AVG(o.total), 0) FROM Order o WHERE o.client.id = :clientId")
    BigDecimal avgTotalByClientId(@Param("clientId") Long clientId);

    @Query("""
        SELECT new com.foodify.server.modules.admin.order.dto.ClientOrderDetailDto(
            o.id,
            o.restaurant.name,
            o.status,
            d.timeToPickUp,
            d.deliveryTime,
            o.total,
            o.orderTime,
            dr.overallRating,
            dr.comments,
            rr.thumbsUp,
            rr.comments
        )
        FROM Order o
        LEFT JOIN o.delivery d
        LEFT JOIN d.rating dr
        LEFT JOIN RestaurantRating rr ON rr.order.id = o.id
        WHERE o.client.id = :clientId
        ORDER BY o.orderTime DESC
        """)
    Page<ClientOrderDetailDto> findOrdersByClientIdWithDetails(@Param("clientId") Long clientId, Pageable pageable);

}