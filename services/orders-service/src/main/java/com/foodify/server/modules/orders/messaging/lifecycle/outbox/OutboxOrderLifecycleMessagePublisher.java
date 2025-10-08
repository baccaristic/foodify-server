package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageFactory;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessagePublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxOrderLifecycleMessagePublisher implements OrderLifecycleMessagePublisher {

    private final OrderLifecycleOutboxService outboxService;

    @Override
    public void publishOrderCreated(Order order, String changedBy) {
        outboxService.enqueue(OrderLifecycleMessageFactory.created(order, changedBy));
    }

    @Override
    public void publishStatusChanged(Order order, OrderStatus previous, OrderStatus current, String changedBy, String reason) {
        outboxService.enqueue(OrderLifecycleMessageFactory.statusChanged(order, previous, current, changedBy, reason));
    }
}
