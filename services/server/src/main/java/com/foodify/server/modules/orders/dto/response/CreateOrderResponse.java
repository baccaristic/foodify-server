package com.foodify.server.modules.orders.dto.response;

import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        OrderStatus status,
        RestaurantSummary restaurant,
        OrderNotificationDTO.DeliverySummary delivery,
        PaymentSummary payment,
        String couponCode,
        List<OrderedItem> items,
        List<com.foodify.server.modules.orders.dto.OrderWorkflowStepDto> workflow
) {
    public record RestaurantSummary(Long id, String name, String imageUrl, String iconUrl) {}

    public record PaymentSummary(
            String method,
            BigDecimal subtotal,
            BigDecimal extrasTotal,
            BigDecimal total,
            BigDecimal itemsSubtotal,
            BigDecimal promotionDiscount,
            BigDecimal couponDiscount,
            BigDecimal itemsTotal,
            BigDecimal deliveryFee
    ) {}

    public record OrderedItem(
            Long menuItemId,
            String name,
            int quantity,
            BigDecimal unitBasePrice,
            BigDecimal unitPrice,
            BigDecimal unitExtrasPrice,
            BigDecimal lineSubtotal,
            BigDecimal promotionDiscount,
            BigDecimal lineItemsTotal,
            BigDecimal extrasTotal,
            BigDecimal lineTotal,
            List<Extra> extras,
            String specialInstructions
    ) {}

    public record Extra(Long id, String name, BigDecimal price) {}
}
