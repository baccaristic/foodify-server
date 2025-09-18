package com.foodify.server.repository;

import com.foodify.server.models.Client;
import com.foodify.server.models.Order;
import com.foodify.server.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByRestaurantOrderByDateDesc(Restaurant restaurant);
    List<Order> findAllByClientOrderByDateDesc(Client client);
    List<Order> findAllByPendingDriverId(Long pendingDriverId);
}
