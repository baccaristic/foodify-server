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
        String restaurantIcon,
        RestaurantSummary restaurant,
        DeliverySummary delivery,
        DeliveryRating rating,
        PaymentSummary payment,
        String couponCode,
        LocalDateTime estimatedReadyAt,
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
            String iconUrl,
            LocationDto location
    ) {}

    public record DeliverySummary(
            Long id,
            DriverSummary driver,
            Long estimatedPickupTime,
            Long estimatedDeliveryTime,
            LocalDateTime estimatedReadyAt,
            LocalDateTime pickupTime,
            LocalDateTime deliveredTime,
            LocationDto driverLocation,
            String address,
            LocationDto location,
            SavedAddressSummaryDto savedAddress
    ) {}

    public record DeliveryRating(
            Integer timing,
            Integer foodCondition,
            Integer professionalism,
            Integer overall,
            String comments,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
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
            BigDecimal couponDiscount,
            BigDecimal itemsTotal,
            BigDecimal deliveryFee,
            BigDecimal serviceFee,
            BigDecimal tipPercentage,
            BigDecimal tipAmount,
            BigDecimal totalBeforeTip,
            BigDecimal cashToCollect,
            String status,
            String paymentUrl,
            String paymentReference,
            String environment
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

