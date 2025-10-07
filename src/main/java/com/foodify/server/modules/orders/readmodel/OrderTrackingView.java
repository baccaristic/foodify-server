package com.foodify.server.modules.orders.readmodel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record OrderTrackingView(
        Long orderId,
        Long clientId,
        String clientName,
        String clientPhone,
        Long restaurantId,
        String restaurantName,
        String currentStatus,
        Instant updatedAt,
        Delivery delivery,
        Amounts amounts,
        List<LineItem> items,
        List<StatusSnapshot> history
) {

    public OrderTrackingView {
        items = items == null ? List.of() : List.copyOf(items);
        history = history == null ? List.of() : List.copyOf(history);
    }

    public OrderTrackingView withStatus(String status, Instant occurredAt, String changedBy, String reason) {
        Instant effectiveTime = occurredAt != null ? occurredAt : Instant.now();
        List<StatusSnapshot> updatedHistory = new ArrayList<>(history != null ? history : Collections.emptyList());
        updatedHistory.add(new StatusSnapshot(status, effectiveTime, changedBy, reason));
        return new OrderTrackingView(
                orderId,
                clientId,
                clientName,
                clientPhone,
                restaurantId,
                restaurantName,
                status,
                effectiveTime,
                delivery,
                amounts,
                items,
                updatedHistory
        );
    }

    public record Delivery(String address, Double lat, Double lng, Long savedAddressId) {
    }

    public record Amounts(BigDecimal itemTotal, BigDecimal extrasTotal, BigDecimal total) {
    }

    public record LineItem(Long menuItemId,
                           String menuItemName,
                           int quantity,
                           BigDecimal unitPrice,
                           BigDecimal extrasTotal,
                           BigDecimal lineTotal,
                           List<ExtraSelection> extras) {

        public LineItem {
            extras = extras == null ? List.of() : List.copyOf(extras);
        }
    }

    public record ExtraSelection(Long id, String name, BigDecimal price) {
    }

    public record StatusSnapshot(String status, Instant occurredAt, String changedBy, String reason) {
        public StatusSnapshot {
            Objects.requireNonNull(status, "status must not be null");
            Objects.requireNonNull(occurredAt, "occurredAt must not be null");
        }
    }
}
