package com.foodify.server.modules.orders.messaging.lifecycle;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpOrderLifecycleMessagePublisher implements OrderLifecycleMessagePublisher {
    @Override
    public void publishOrderCreated(Order order, String changedBy) {
        log.debug("Order lifecycle messaging disabled. Skipping ORDER_CREATED for order {}", order != null ? order.getId() : null);
    }

    @Override
    public void publishStatusChanged(Order order, OrderStatus previous, OrderStatus current, String changedBy, String reason) {
        log.debug("Order lifecycle messaging disabled. Skipping ORDER_STATUS_CHANGED for order {}", order != null ? order.getId() : null);
    }
}
