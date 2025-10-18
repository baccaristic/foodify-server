package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.orders.domain.OrderLifecycleAction;
import com.foodify.server.modules.orders.domain.OrderStatus;

import java.math.BigDecimal;
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
        String restaurantImage,
        RestaurantSummary restaurant,
        DeliverySummary delivery,
        PaymentSummary payment,
        List<OrderStatusHistoryDTO> statusHistory,
        String deliveryToken,
        String pickupToken
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
            LocationDto driverLocation,
            String address,
            LocationDto location,
            SavedAddressSummaryDto savedAddress
    ) {}

    public record DriverSummary(
            Long id,
            String name,
            String phone
    ) {}

    public record PaymentSummary(
            BigDecimal subtotal,
            BigDecimal extrasTotal,
            BigDecimal total,
            BigDecimal itemsSubtotal,
            BigDecimal promotionDiscount,
            BigDecimal itemsTotal,
            BigDecimal deliveryFee
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
