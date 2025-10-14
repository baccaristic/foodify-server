package com.foodify.server.modules.orders.support;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class OrderPricingCalculator {

    private OrderPricingCalculator() {
    }

    public static BigDecimal calculateTotal(Order order) {
        return calculatePricing(order).itemsTotal();
    }

    public static BigDecimal calculateTotal(Collection<OrderItem> orderItems) {
        return calculatePricing(orderItems).itemsTotal();
    }

    public static OrderPricingBreakdown calculatePricing(Order order) {
        if (order == null) {
            return OrderPricingBreakdown.empty();
        }
        return calculatePricing(order.getItems());
    }

    public static OrderPricingBreakdown calculatePricing(Collection<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return OrderPricingBreakdown.empty();
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal promotionalSubtotal = BigDecimal.ZERO;
        BigDecimal extrasTotal = BigDecimal.ZERO;

        for (OrderItem item : orderItems) {
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal basePrice = resolveBasePrice(item);
            BigDecimal promotionalPrice = resolvePromotionalPrice(item, basePrice);
            BigDecimal extrasPerUnit = resolveExtrasPerUnit(item);

            subtotal = subtotal.add(basePrice.multiply(quantity));
            promotionalSubtotal = promotionalSubtotal.add(promotionalPrice.multiply(quantity));
            extrasTotal = extrasTotal.add(extrasPerUnit.multiply(quantity));
        }

        BigDecimal promotionDiscount = subtotal.subtract(promotionalSubtotal);
        if (promotionDiscount.compareTo(BigDecimal.ZERO) < 0) {
            promotionDiscount = BigDecimal.ZERO;
        }

        BigDecimal itemsTotal = promotionalSubtotal.add(extrasTotal);

        return new OrderPricingBreakdown(
                subtotal.setScale(2, RoundingMode.HALF_UP),
                extrasTotal.setScale(2, RoundingMode.HALF_UP),
                promotionDiscount.setScale(2, RoundingMode.HALF_UP),
                itemsTotal.setScale(2, RoundingMode.HALF_UP)
        );
    }

    private static BigDecimal resolveBasePrice(OrderItem orderItem) {
        if (orderItem == null || orderItem.getMenuItem() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(orderItem.getMenuItem().getPrice());
    }

    private static BigDecimal resolvePromotionalPrice(OrderItem orderItem, BigDecimal fallback) {
        if (orderItem == null || orderItem.getMenuItem() == null) {
            return BigDecimal.ZERO;
        }

        if (Boolean.TRUE.equals(orderItem.getMenuItem().getPromotionActive())
                && orderItem.getMenuItem().getPromotionPrice() != null) {
            return BigDecimal.valueOf(orderItem.getMenuItem().getPromotionPrice());
        }

        return fallback;
    }

    private static BigDecimal resolveExtrasPerUnit(OrderItem item) {
        return Optional.ofNullable(item)
                .map(OrderItem::getMenuItemExtras)
                .orElse(Collections.emptyList())
                .stream()
                .map(MenuItemExtra::getPrice)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateDriverShare(BigDecimal totalAmount, BigDecimal commissionRate) {
        if (totalAmount == null || commissionRate == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return totalAmount.multiply(commissionRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateOfficeShare(BigDecimal totalAmount, BigDecimal driverShare) {
        BigDecimal safeTotal = Optional.ofNullable(totalAmount)
                .orElse(BigDecimal.ZERO);
        BigDecimal safeDriverShare = Optional.ofNullable(driverShare)
                .orElse(BigDecimal.ZERO);
        return safeTotal.subtract(safeDriverShare)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public record OrderPricingBreakdown(
            BigDecimal itemsSubtotal,
            BigDecimal extrasTotal,
            BigDecimal promotionDiscount,
            BigDecimal itemsTotal
    ) {
        public static OrderPricingBreakdown empty() {
            BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return new OrderPricingBreakdown(zero, zero, zero, zero);
        }

        public BigDecimal subtotalBeforeDiscount() {
            return itemsSubtotal.add(extrasTotal).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
