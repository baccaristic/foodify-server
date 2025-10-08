package com.foodify.server.modules.orders.messaging.lifecycle;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOrderLifecycleMessagePublisher implements OrderLifecycleMessagePublisher {

    private final OrderLifecycleMessageSender sender;

    @Override
    public void publishOrderCreated(Order order, String changedBy) {
        sender.send(OrderLifecycleMessageFactory.created(order, changedBy));
    }

    @Override
    public void publishStatusChanged(Order order, OrderStatus previous, OrderStatus current, String changedBy, String reason) {
        sender.send(OrderLifecycleMessageFactory.statusChanged(order, previous, current, changedBy, reason));
    }
}
