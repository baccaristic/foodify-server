package com.foodify.server.modules.orders.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemRequest {
    private Long menuItemId;
    private int quantity;
    private String specialInstructions;

    // Explicitly selected extras by the client
    private List<Long> extraIds;
}
