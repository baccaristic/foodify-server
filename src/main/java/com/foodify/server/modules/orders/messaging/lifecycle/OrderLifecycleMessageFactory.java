package com.foodify.server.modules.orders.messaging.lifecycle;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class OrderLifecycleMessageFactory {

    public static final String EVENT_TYPE_CREATED = "ORDER_CREATED";
    public static final String EVENT_TYPE_STATUS_CHANGED = "ORDER_STATUS_CHANGED";

    private OrderLifecycleMessageFactory() {
    }

    public static OrderLifecycleMessage created(Order order, String changedBy) {
        return buildMessage(order,
                EVENT_TYPE_CREATED,
                null,
                order.getStatus(),
                changedBy,
                "Order created");
    }

    public static OrderLifecycleMessage statusChanged(Order order,
                                                       OrderStatus previous,
                                                       OrderStatus current,
                                                       String changedBy,
                                                       String reason) {
        return buildMessage(order,
                EVENT_TYPE_STATUS_CHANGED,
                previous,
                current,
                changedBy,
                reason);
    }

    private static OrderLifecycleMessage buildMessage(Order order,
                                                       String type,
                                                       OrderStatus previous,
                                                       OrderStatus current,
                                                       String changedBy,
                                                       String reason) {
        List<OrderLifecycleMessage.OrderLineItem> lineItems = mapLineItems(order.getItems());
        OrderLifecycleMessage.OrderAmounts amounts = calculateAmounts(lineItems);
        OrderLifecycleMessage.OrderDelivery delivery = mapDelivery(order);

        return new OrderLifecycleMessage(
                UUID.randomUUID(),
                type,
                order.getId(),
                order.getClient() != null ? order.getClient().getId() : null,
                order.getRestaurant() != null ? order.getRestaurant().getId() : null,
                previous != null ? previous.name() : null,
                current != null ? current.name() : null,
                changedBy,
                reason,
                resolveOccurredAt(order.getDate(), order.getOrderTime()),
                delivery,
                amounts,
                lineItems
        );
    }

    private static OrderLifecycleMessage.OrderDelivery mapDelivery(Order order) {
        return new OrderLifecycleMessage.OrderDelivery(
                order.getDeliveryAddress(),
                order.getLat(),
                order.getLng(),
                order.getSavedAddress() != null ? order.getSavedAddress().getId() : null
        );
    }

    private static OrderLifecycleMessage.OrderAmounts calculateAmounts(List<OrderLifecycleMessage.OrderLineItem> lineItems) {
        BigDecimal itemTotal = lineItems.stream()
                .map(OrderLifecycleMessage.OrderLineItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal extrasTotal = lineItems.stream()
                .map(OrderLifecycleMessage.OrderLineItem::extrasTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderLifecycleMessage.OrderAmounts(itemTotal, extrasTotal, itemTotal);
    }

    private static List<OrderLifecycleMessage.OrderLineItem> mapLineItems(List<OrderItem> items) {
        List<OrderLifecycleMessage.OrderLineItem> result = new ArrayList<>();
        if (items == null) {
            return result;
        }
        for (OrderItem item : items) {
            MenuItem menuItem = item.getMenuItem();
            BigDecimal basePrice = resolveMenuItemPrice(menuItem);
            List<OrderLifecycleMessage.ExtraSelection> extras = mapExtras(item.getMenuItemExtras());
            BigDecimal extrasTotal = extras.stream()
                    .map(OrderLifecycleMessage.ExtraSelection::price)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal unitTotal = basePrice.add(extrasTotal);
            BigDecimal lineTotal = unitTotal.multiply(BigDecimal.valueOf(item.getQuantity()));
            result.add(new OrderLifecycleMessage.OrderLineItem(
                    menuItem != null ? menuItem.getId() : null,
                    menuItem != null ? menuItem.getName() : null,
                    item.getQuantity(),
                    basePrice,
                    extrasTotal,
                    lineTotal,
                    extras
            ));
        }
        return result;
    }

    private static List<OrderLifecycleMessage.ExtraSelection> mapExtras(List<MenuItemExtra> extras) {
        List<OrderLifecycleMessage.ExtraSelection> result = new ArrayList<>();
        if (extras == null) {
            return result;
        }
        for (MenuItemExtra extra : extras) {
            result.add(new OrderLifecycleMessage.ExtraSelection(
                    extra.getId(),
                    extra.getName(),
                    toBigDecimal(extra.getPrice())
            ));
        }
        return result;
    }

    private static BigDecimal resolveMenuItemPrice(MenuItem menuItem) {
        if (menuItem == null) {
            return BigDecimal.ZERO;
        }
        if (Boolean.TRUE.equals(menuItem.getPromotionActive()) && menuItem.getPromotionPrice() != null) {
            return toBigDecimal(menuItem.getPromotionPrice());
        }
        return toBigDecimal(menuItem.getPrice());
    }

    private static BigDecimal toBigDecimal(Double value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private static Instant resolveOccurredAt(LocalDateTime date, LocalDateTime fallback) {
        LocalDateTime reference = date != null ? date : fallback;
        if (reference == null) {
            return Instant.now();
        }
        return reference.atZone(ZoneId.systemDefault()).toInstant();
    }
}
