package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.orders.domain.OrderLifecycleAction;
import com.foodify.server.modules.orders.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderNotificationDTO(
        Long orderId,
        String deliveryAddress,
        String paymentMethod,
        LocalDateTime date,
        List<OrderItemDTO> items,
        SavedAddressSummaryDto savedAddress,
        ClientSummaryDTO client,
        OrderStatus status,
        LocationDto deliveryLocation,
        RestaurantSummary restaurant,
        DeliverySummary delivery,
        List<OrderStatusHistoryDTO> statusHistory
) {

    public record RestaurantSummary(
            Long id,
            String name,
            String address,
            String phone,
            String imageUrl,
            LocationDto location
    ) {}

    public record DeliverySummary(
            Long id,
            DriverSummary driver,
            Long estimatedPickupTime,
            Long estimatedDeliveryTime,
            LocalDateTime pickupTime,
            LocalDateTime deliveredTime,
            LocationDto driverLocation
    ) {}

    public record DriverSummary(
            Long id,
            String name,
            String phone
    ) {}

    public record OrderStatusHistoryDTO(
            OrderLifecycleAction action,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String changedBy,
            String reason,
            String metadata,
            LocalDateTime changedAt
    ) {}
}
