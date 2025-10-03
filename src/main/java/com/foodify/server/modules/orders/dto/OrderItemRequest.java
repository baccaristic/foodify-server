package com.foodify.server.modules.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemRequest {

    @NotNull(message = "Menu item id is required")
    private Long menuItemId;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;

    private String specialInstructions;

    // Explicitly selected extras by the client
    private List<Long> extraIds;
}
