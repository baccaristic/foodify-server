package com.foodify.server.modules.orders.dto.response;

import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.LocationDto;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        OrderStatus status,
        RestaurantSummary restaurant,
        DeliverySummary delivery,
        PaymentSummary payment,
        List<OrderedItem> items,
        List<com.foodify.server.modules.orders.dto.OrderWorkflowStepDto> workflow
) {
    public record RestaurantSummary(Long id, String name, String imageUrl) {}

    public record DeliverySummary(String address, LocationDto location) {}

    public record PaymentSummary(String method, BigDecimal subtotal, BigDecimal extrasTotal, BigDecimal total) {}

    public record OrderedItem(
            Long menuItemId,
            String name,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal extrasPrice,
            BigDecimal lineTotal,
            List<Extra> extras,
            String specialInstructions
    ) {}

    public record Extra(Long id, String name, BigDecimal price) {}
}
