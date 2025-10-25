package com.foodify.server.modules.orders.repository;

import com.foodify.server.modules.orders.domain.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = {"menuItem", "menuItemExtras"})
    List<OrderItem> findByOrder_IdIn(Collection<Long> orderIds);
}
