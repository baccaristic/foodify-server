package com.foodify.server.modules.orders.messaging.lifecycle;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;

public interface OrderLifecycleMessagePublisher {

    void publishOrderCreated(Order order, String changedBy);

    void publishStatusChanged(Order order, OrderStatus previous, OrderStatus current, String changedBy, String reason);
}
