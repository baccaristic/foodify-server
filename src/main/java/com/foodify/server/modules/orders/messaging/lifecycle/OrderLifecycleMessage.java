package com.foodify.server.modules.orders.messaging.lifecycle;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderLifecycleMessage(
        UUID eventId,
        String type,
        Long orderId,
        Long clientId,
        String clientName,
        String clientPhone,
        Long restaurantId,
        String restaurantName,
        String previousStatus,
        String currentStatus,
        String changedBy,
        String reason,
        Instant occurredAt,
        OrderDelivery delivery,
        OrderAmounts amounts,
        List<OrderLineItem> items
) {

    public record OrderDelivery(String address, Double lat, Double lng, Long savedAddressId) {
    }

    public record OrderAmounts(BigDecimal itemTotal, BigDecimal extrasTotal, BigDecimal total) {
    }

    public record OrderLineItem(Long menuItemId,
                                String menuItemName,
                                int quantity,
                                BigDecimal unitPrice,
                                BigDecimal extrasTotal,
                                BigDecimal lineTotal,
                                List<ExtraSelection> extras) {
    }

    public record ExtraSelection(Long id, String name, BigDecimal price) {
    }
}
