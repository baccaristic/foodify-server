package com.foodify.server.modules.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderNotificationDTO(
        Long orderId,
        String deliveryAddress,
        String paymentMethod,
        LocalDateTime date,
        List<OrderItemDTO> items,
        ClientSummaryDTO client
) {}
