package com.foodify.server.modules.orders.dto;


import java.util.List;

public record OrderItemDTO(
        Long menuItemId,
        String menuItemName,
        int quantity,
        List<String> extras,
        String specialInstructions
) {}
