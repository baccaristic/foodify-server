package com.foodify.server.modules.orders.support;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Optional;

public final class OrderPricingCalculator {
    private OrderPricingCalculator() {
    }

    public static BigDecimal calculateTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            if (item == null) {
                continue;
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
            total = total.add(lineSubtotal).add(lineExtras);
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
