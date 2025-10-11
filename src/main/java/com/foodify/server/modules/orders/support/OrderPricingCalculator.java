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
    private static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private OrderPricingCalculator() {
    }

    public static BigDecimal calculateTotal(Order order) {
        if (order == null) {
            return ZERO_AMOUNT;
        }

        return calculateTotal(order.getItems());
    }

    public static BigDecimal calculateTotal(Collection<OrderItem> orderItems) {
        if (orderItems == null) {
            return ZERO_AMOUNT;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(calculateLineTotal(item));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal resolveUnitPrice(OrderItem orderItem) {
        if (orderItem == null || orderItem.getMenuItem() == null) {
            return BigDecimal.ZERO;
        }

        if (Boolean.TRUE.equals(orderItem.getMenuItem().getPromotionActive())
                && orderItem.getMenuItem().getPromotionPrice() != null) {
            return BigDecimal.valueOf(orderItem.getMenuItem().getPromotionPrice());
        }

        return BigDecimal.valueOf(orderItem.getMenuItem().getPrice());
    }

    private static BigDecimal calculateLineTotal(OrderItem item) {
        if (item == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
        BigDecimal unitPrice = resolveUnitPrice(item);
        BigDecimal extrasPerUnit = Optional.ofNullable(item.getMenuItemExtras())
                .orElse(Collections.emptyList())
                .stream()
                .map(MenuItemExtra::getPrice)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lineSubtotal = unitPrice.multiply(quantity);
        BigDecimal lineExtras = extrasPerUnit.multiply(quantity);
        return lineSubtotal.add(lineExtras);
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
}
