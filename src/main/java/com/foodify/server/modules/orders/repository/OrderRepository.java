package com.foodify.server.modules.orders.repository;

import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByRestaurantOrderByDateDesc(Restaurant restaurant);
    List<Order> findAllByClientOrderByDateDesc(Client client);
    List<Order> findAllByPendingDriverId(Long pendingDriverId);
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
    SELECT DISTINCT o
    FROM Order o
    JOIN FETCH o.items i
    JOIN o.delivery d
    WHERE d.driver.id = :driverId 
      AND o.status = :status
    ORDER BY o.date DESC
""")
    List<Order> findAllByDriverIdAndStatus(Long driverId, OrderStatus status);
}
