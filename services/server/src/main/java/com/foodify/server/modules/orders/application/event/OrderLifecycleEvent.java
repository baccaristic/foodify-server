package com.foodify.server.modules.orders.application.event;

import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderLifecycleEvent {
    private final Long orderId;
    private final OrderStatus previousStatus;
    private final OrderStatus newStatus;
    private final String changedBy;
    private final String reason;
}
