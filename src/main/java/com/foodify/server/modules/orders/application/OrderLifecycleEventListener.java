package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.notifications.application.NotificationPreferenceService;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import io.micrometer.core.annotation.Timed;
import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrderLifecycleEventListener {

    private final OrderRepository orderRepository;
    private final WebSocketService webSocketService;
    private final UserDeviceService userDeviceService;
    private final PushNotificationService pushNotificationService;
    private final NotificationPreferenceService notificationPreferenceService;
    private final TaskExecutor notificationExecutor;

    public OrderLifecycleEventListener(
            OrderRepository orderRepository,
            WebSocketService webSocketService,
            UserDeviceService userDeviceService,
            PushNotificationService pushNotificationService,
            NotificationPreferenceService notificationPreferenceService,
            @Qualifier("notificationDispatchExecutor") TaskExecutor notificationExecutor
    ) {
        this.orderRepository = orderRepository;
        this.webSocketService = webSocketService;
        this.userDeviceService = userDeviceService;
        this.pushNotificationService = pushNotificationService;
        this.notificationPreferenceService = notificationPreferenceService;
        this.notificationExecutor = notificationExecutor;
    }

    private static final Map<OrderStatus, NotificationTemplate> CLIENT_NOTIFICATION_TEMPLATES = buildClientTemplates();

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Async("orderLifecycleExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Timed(value = "orders.lifecycle.notifications", description = "Time spent broadcasting order lifecycle events", histogram = true)
    public void handleOrderLifecycleEvent(OrderLifecycleEvent event) {
        Order order = orderRepository.findDetailedById(event.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found for notification"));

        notifyRestaurant(order, event);
        notifyDriver(order);
        notifyClient(order, event);
    }

    private void notifyRestaurant(Order order, OrderLifecycleEvent event) {
        if (order.getRestaurant() == null || order.getRestaurant().getId() == null) {
            return;
        }
        Long restaurantId = order.getRestaurant().getId();
        webSocketService.notifyRestaurant(restaurantId, order);

        if (event.getPreviousStatus() == null) {
            webSocketService.notifyRestaurantNewOrder(restaurantId, order);
        }
    }

    private void notifyDriver(Order order) {
        if (order.getDelivery() != null && order.getDelivery().getDriver() != null) {
            webSocketService.notifyDriver(order.getDelivery().getDriver().getId(), order);
        } else if (order.getPendingDriver() != null) {
            webSocketService.notifyDriver(order.getPendingDriver().getId(), order);
        }
    }

    private void notifyClient(Order order, OrderLifecycleEvent event) {
        if (order.getClient() == null) {
            return;
        }

        webSocketService.notifyClient(order.getClient().getId(), order);
        try {
            pushNotificationService.sendPushNotification(order.getClient().getId(), "Your order is updated", "Order has been updated");
        } catch (IOException e) {
            log.debug("Notification Service: Expo failed with reason: " + e.getMessage());
        }

        NotificationTemplate template = CLIENT_NOTIFICATION_TEMPLATES.get(event.getNewStatus());
        if (template == null) {
            return;
        }

        if (!notificationPreferenceService.isEnabled(order.getClient().getId(), NotificationType.ORDER_UPDATES)) {
            return;
        }

        List<UserDevice> devices = userDeviceService.findByUser(order.getClient().getId());
        if (devices.isEmpty()) {
            return;
        }

        Long orderId = order.getId();
        for (UserDevice device : devices) {
            notificationExecutor.execute(() -> {
                try {
                    pushNotificationService.sendOrderNotification(
                            device.getDeviceToken(),
                            orderId,
                            template.title,
                            template.body,
                            NotificationType.ORDER_UPDATES
                    );
                } catch (Exception ex) {
                    log.warn("Failed to push notification for order {} to device {}", orderId, device.getDeviceToken(), ex);
                }
            });
        }
    }

    private static Map<OrderStatus, NotificationTemplate> buildClientTemplates() {
        Map<OrderStatus, NotificationTemplate> templates = new EnumMap<>(OrderStatus.class);
        templates.put(OrderStatus.ACCEPTED, new NotificationTemplate(
                "Order accepted",
                "The restaurant accepted your order. We'll notify you as it progresses."
        ));
        templates.put(OrderStatus.PREPARING, new NotificationTemplate(
                "Driver assigned",
                "A driver has been assigned to your order."
        ));
        templates.put(OrderStatus.READY_FOR_PICK_UP, new NotificationTemplate(
                "Order ready",
                "Your order is ready for pickup and will be on its way soon."
        ));
        templates.put(OrderStatus.IN_DELIVERY, new NotificationTemplate(
                "Order on the way",
                "Your driver has picked up the order and is heading to you."
        ));
        templates.put(OrderStatus.DELIVERED, new NotificationTemplate(
                "Order delivered",
                "Enjoy your meal! The driver has marked the order as delivered."
        ));
        templates.put(OrderStatus.CANCELED, new NotificationTemplate(
                "Order canceled",
                "This order has been canceled. Contact support if you need assistance."
        ));
        templates.put(OrderStatus.REJECTED, new NotificationTemplate(
                "Order rejected",
                "The restaurant couldn't fulfill this order. You won't be charged."
        ));
        return templates;
    }

    private record NotificationTemplate(String title, String body) {
    }
}
