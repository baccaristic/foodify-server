package com.foodify.server.modules.orders.domain;

public enum OrderStatus {
    PENDING,
    ACCEPTED,
    PREPARING,
    READY_FOR_PICK_UP,
    IN_DELIVERY,
    DELIVERED,
    REJECTED,
    CANCELED
}
