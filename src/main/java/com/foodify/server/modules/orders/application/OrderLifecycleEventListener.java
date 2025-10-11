package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.notifications.application.NotificationPreferenceService;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderLifecycleEventListener {

    private final OrderRepository orderRepository;
    private final OrderNotificationMapper orderNotificationMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketService webSocketService;
    private final UserDeviceService userDeviceService;
    private final PushNotificationService pushNotificationService;
    private final NotificationPreferenceService notificationPreferenceService;

    private static final Map<OrderStatus, NotificationTemplate> CLIENT_NOTIFICATION_TEMPLATES = buildClientTemplates();

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderLifecycleEvent(OrderLifecycleEvent event) {
        Order order = orderRepository.findDetailedById(event.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found for notification"));

        notifyRestaurant(order);
        notifyDriver(order);
        notifyClient(order, event);
    }

    private void notifyRestaurant(Order order) {
        if (order.getRestaurant() == null || order.getRestaurant().getAdmin() == null) {
            return;
        }
        messagingTemplate.convertAndSend(
                "/topic/orders/" + order.getRestaurant().getAdmin().getId(),
                orderNotificationMapper.toDto(order)
        );
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

        NotificationTemplate template = CLIENT_NOTIFICATION_TEMPLATES.get(event.getNewStatus());
        if (template == null) {
            return;
        }

        if (!notificationPreferenceService.isEnabled(order.getClient().getId(), template.type())) {
            return;
        }

        List<UserDevice> devices = userDeviceService.findByUser(order.getClient().getId());
        if (devices.isEmpty()) {
            return;
        }

        for (UserDevice device : devices) {
            try {
                pushNotificationService.sendOrderNotification(
                        device.getDeviceToken(),
                        order.getId(),
                        template.title,
                        template.body,
                        template.type
                );
            } catch (Exception ex) {
                log.warn("Failed to push notification for order {} to device {}", order.getId(), device.getDeviceToken(), ex);
            }
        }
    }

    private static Map<OrderStatus, NotificationTemplate> buildClientTemplates() {
        Map<OrderStatus, NotificationTemplate> templates = new EnumMap<>(OrderStatus.class);
        templates.put(OrderStatus.ACCEPTED, new NotificationTemplate(
                "Order accepted",
                "The restaurant accepted your order. We'll notify you as it progresses.",
                NotificationType.ORDER_CLIENT_ORDER_ACCEPTED
        ));
        templates.put(OrderStatus.PREPARING, new NotificationTemplate(
                "Driver assigned",
                "A driver has been assigned to your order.",
                NotificationType.ORDER_DRIVER_ASSIGNED
        ));
        templates.put(OrderStatus.READY_FOR_PICK_UP, new NotificationTemplate(
                "Order ready",
                "Your order is ready for pickup and will be on its way soon.",
                NotificationType.ORDER_CLIENT_ORDER_READY
        ));
        templates.put(OrderStatus.IN_DELIVERY, new NotificationTemplate(
                "Order on the way",
                "Your driver has picked up the order and is heading to you.",
                NotificationType.ORDER_CLIENT_ORDER_EN_ROUTE
        ));
        templates.put(OrderStatus.DELIVERED, new NotificationTemplate(
                "Order delivered",
                "Enjoy your meal! The driver has marked the order as delivered.",
                NotificationType.ORDER_CLIENT_ORDER_DELIVERED
        ));
        templates.put(OrderStatus.CANCELED, new NotificationTemplate(
                "Order canceled",
                "This order has been canceled. Contact support if you need assistance.",
                NotificationType.ORDER_CLIENT_ORDER_CANCELED
        ));
        templates.put(OrderStatus.REJECTED, new NotificationTemplate(
                "Order rejected",
                "The restaurant couldn't fulfill this order. You won't be charged.",
                NotificationType.ORDER_CLIENT_ORDER_REJECTED
        ));
        return templates;
    }

    private record NotificationTemplate(String title, String body, NotificationType type) {
    }
}
