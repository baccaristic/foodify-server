package com.foodify.server.modules.orders.repository;

import com.foodify.server.modules.orders.domain.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findAllByOrderIdOrderByChangedAtAsc(Long orderId);
}
