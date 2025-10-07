package com.foodify.server.modules.orders.readmodel;

import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class OrderTrackingViewFactory {

    private OrderTrackingViewFactory() {
    }

    public static OrderTrackingView project(OrderLifecycleMessage message,
                                            OrderTrackingView current) {
        Objects.requireNonNull(message, "message must not be null");
        if (current == null) {
            return create(message);
        }
        return update(current, message);
    }

    private static OrderTrackingView create(OrderLifecycleMessage message) {
        String status = Objects.requireNonNullElse(message.currentStatus(), message.previousStatus());
        if (status == null) {
            throw new IllegalStateException("Unable to resolve order status for tracking view");
        }
        List<OrderTrackingView.StatusSnapshot> history = new ArrayList<>();
        history.add(new OrderTrackingView.StatusSnapshot(
                status,
                defaultOccurredAt(message.occurredAt()),
                message.changedBy(),
                message.reason()
        ));
        return new OrderTrackingView(
                message.orderId(),
                message.clientId(),
                message.clientName(),
                message.clientPhone(),
                message.restaurantId(),
                message.restaurantName(),
                status,
                defaultOccurredAt(message.occurredAt()),
                mapDelivery(message),
                mapAmounts(message),
                mapLineItems(message),
                history
        );
    }

    private static OrderTrackingView update(OrderTrackingView current, OrderLifecycleMessage message) {
        String status = message.currentStatus();
        if (status == null) {
            status = current.currentStatus();
        }
        return current.withStatus(
                status,
                defaultOccurredAt(message.occurredAt()),
                message.changedBy(),
                message.reason()
        );
    }

    private static OrderTrackingView.Delivery mapDelivery(OrderLifecycleMessage message) {
        OrderLifecycleMessage.OrderDelivery delivery = message.delivery();
        if (delivery == null) {
            return new OrderTrackingView.Delivery(null, null, null, null);
        }
        return new OrderTrackingView.Delivery(
                delivery.address(),
                delivery.lat(),
                delivery.lng(),
                delivery.savedAddressId()
        );
    }

    private static OrderTrackingView.Amounts mapAmounts(OrderLifecycleMessage message) {
        OrderLifecycleMessage.OrderAmounts amounts = message.amounts();
        if (amounts == null) {
            return new OrderTrackingView.Amounts(null, null, null);
        }
        return new OrderTrackingView.Amounts(
                amounts.itemTotal(),
                amounts.extrasTotal(),
                amounts.total()
        );
    }

    private static List<OrderTrackingView.LineItem> mapLineItems(OrderLifecycleMessage message) {
        List<OrderTrackingView.LineItem> items = new ArrayList<>();
        if (message.items() == null) {
            return items;
        }
        for (OrderLifecycleMessage.OrderLineItem item : message.items()) {
            items.add(new OrderTrackingView.LineItem(
                    item.menuItemId(),
                    item.menuItemName(),
                    item.quantity(),
                    item.unitPrice(),
                    item.extrasTotal(),
                    item.lineTotal(),
                    mapExtras(item)
            ));
        }
        return items;
    }

    private static List<OrderTrackingView.ExtraSelection> mapExtras(OrderLifecycleMessage.OrderLineItem item) {
        List<OrderTrackingView.ExtraSelection> extras = new ArrayList<>();
        if (item.extras() == null) {
            return extras;
        }
        for (OrderLifecycleMessage.ExtraSelection extra : item.extras()) {
            extras.add(new OrderTrackingView.ExtraSelection(extra.id(), extra.name(), extra.price()));
        }
        return extras;
    }

    private static Instant defaultOccurredAt(Instant occurredAt) {
        return occurredAt != null ? occurredAt : Instant.now();
    }

}
