package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.orders.domain.OrderStatus;

public record OrderWorkflowStepDto(
        OrderStatus status,
        String title,
        String description,
        boolean completed,
        boolean current
) {}
