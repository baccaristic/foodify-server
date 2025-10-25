package com.foodify.server.modules.orders.support;

import com.foodify.server.modules.orders.domain.OrderStatus;

import java.util.List;

public final class OrderStatusGroups {

    private OrderStatusGroups() {
    }

    public static final List<OrderStatus> RESTAURANT_ACTIVE_STATUSES = List.of(
            OrderStatus.PENDING,
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );
}
