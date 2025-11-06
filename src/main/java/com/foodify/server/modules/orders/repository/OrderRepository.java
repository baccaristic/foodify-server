package com.foodify.server.modules.orders.repository;

import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Page<Order> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Page<Order> findAllByRestaurantAndDateGreaterThanEqual(
            Restaurant restaurant,
            LocalDateTime fromDate,
            Pageable pageable
    );

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Page<Order> findAllByRestaurantAndDateLessThanEqual(
            Restaurant restaurant,
            LocalDateTime toDate,
            Pageable pageable
    );

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Page<Order> findAllByRestaurantAndDateBetween(
            Restaurant restaurant,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Page<Order> findAllByClient(Client client, Pageable pageable);
    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Slice<Order> findAllByPendingDriverId(Long pendingDriverId, Pageable pageable);
    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Optional<Order> findDetailedById(Long id);
    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Optional<Order> findByPaymentReference(String paymentReference);
    boolean existsByClient_IdAndStatusInAndArchivedAtIsNull(Long clientId, List<OrderStatus> statuses);
    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Optional<Order> findFirstByClient_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(Long clientId, List<OrderStatus> statuses);

    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    Slice<Order> findAllByRestaurant_Admin_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(
            Long adminId,
            List<OrderStatus> statuses,
            Pageable pageable
    );
    @Query("""
    SELECT o
    FROM Order o
    JOIN FETCH o.items i
    JOIN o.delivery d
    WHERE d.driver.id = :driverId 
      AND o.status IN :statuses
""")
    Optional<Order> findByDriverIdAndStatusIn(Long driverId, List<OrderStatus> statuses);
    @Query("""
    SELECT DISTINCT d.driver.id
    FROM Order o
    JOIN o.delivery d
    WHERE d.driver.id IN :driverIds
      AND o.status IN :statuses
""")
    Set<Long> findDriverIdsByStatusIn(
            @Param("driverIds") Collection<Long> driverIds,
            @Param("statuses") Collection<OrderStatus> statuses
    );
    @Query("""
    SELECT DISTINCT o
    FROM Order o
    JOIN FETCH o.items i
    JOIN o.delivery d
    WHERE d.driver.id = :driverId
      AND o.status = :status
    ORDER BY o.date DESC
    """)
    List<Order> findAllByDriverIdAndStatus(Long driverId, OrderStatus status);

    long countByStatusInAndDeliveryIsNullAndPendingDriverIsNullAndArchivedAtIsNull(Collection<OrderStatus> statuses);

    long countByStatusInAndDeliveryIsNullAndPendingDriverIsNotNullAndArchivedAtIsNull(Collection<OrderStatus> statuses);

    @Query("""
    SELECT o.id
    FROM Order o
    WHERE o.status IN :statuses
      AND o.delivery IS NULL
      AND o.pendingDriver IS NULL
      AND o.archivedAt IS NULL
    """)
    List<Long> findIdsNeedingDriver(@Param("statuses") Collection<OrderStatus> statuses);
    @Query("""
    SELECT o
    FROM Order o
    WHERE o.status IN :statuses
      AND o.archivedAt IS NULL
      AND o.date < :date
    ORDER BY o.id
    """)
    Slice<Order> findArchivableOrders(
            @Param("statuses") Collection<OrderStatus> statuses,
            @Param("date") LocalDateTime date,
            Pageable pageable
    );
    
    @Query("""
    SELECT COUNT(o) FROM Order o
    JOIN o.delivery d
    WHERE d.driver.id = :driverId
    AND o.status = :status
    """)
    long countByDriverIdAndStatus(@Param("driverId") Long driverId, @Param("status") OrderStatus status);
    
    @Query("""
    SELECT COALESCE(SUM(o.total), 0) FROM Order o
    WHERE o.restaurant.id = :restaurantId
    AND o.status = 'DELIVERED'
    AND o.date >= :startDate AND o.date < :endDate
    """)
    java.math.BigDecimal getTotalRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("""
    SELECT COUNT(o) FROM Order o
    WHERE o.restaurant.id = :restaurantId
    AND o.status = 'DELIVERED'
    AND o.date >= :startDate AND o.date < :endDate
    """)
    long countDeliveredOrdersByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("""
    SELECT COALESCE(AVG(o.total), 0) FROM Order o
    WHERE o.restaurant.id = :restaurantId
    AND o.status = 'DELIVERED'
    AND o.date >= :startDate AND o.date < :endDate
    """)
    Double getAverageOrderValueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    @EntityGraph(value = Order.SUMMARY_GRAPH, type = EntityGraphType.LOAD)
    @Query("""
    SELECT o FROM Order o
    WHERE o.restaurant.id = :restaurantId
    AND (:status IS NULL OR o.status = :status)
    AND CAST(o.date AS date) = CAST(:date AS date)
    ORDER BY o.date DESC
    """)
    Page<Order> findByRestaurantIdAndDateAndStatus(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDateTime date,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
