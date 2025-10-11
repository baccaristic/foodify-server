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
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLifecycleEventListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private UserDeviceService userDeviceService;

    @Mock
    private PushNotificationService pushNotificationService;

    @Mock
    private NotificationPreferenceService notificationPreferenceService;

    private OrderLifecycleEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new OrderLifecycleEventListener(
                orderRepository,
                new OrderNotificationMapper(),
                messagingTemplate,
                webSocketService,
                userDeviceService,
                pushNotificationService,
                notificationPreferenceService
        );
    }

    @Test
    void shouldNotifyClientViaWebSocketAndPushWhenTemplateExists() throws Exception {
        Order order = buildOrderWithClient(1L, 5L);
        UserDevice device = new UserDevice();
        device.setDeviceToken("token");

        when(orderRepository.findDetailedById(1L)).thenReturn(Optional.of(order));
        when(userDeviceService.findByUser(5L)).thenReturn(List.of(device));
        when(notificationPreferenceService.isEnabled(5L, NotificationType.ORDER_UPDATES)).thenReturn(true);

        OrderLifecycleEvent event = new OrderLifecycleEvent(1L, OrderStatus.PENDING, OrderStatus.ACCEPTED, "system", null);

        listener.handleOrderLifecycleEvent(event);

        verify(webSocketService).notifyClient(5L, order);
        verify(pushNotificationService).sendOrderNotification(
                eq("token"),
                eq(1L),
                eq("Order accepted"),
                eq("The restaurant accepted your order. We'll notify you as it progresses."),
                eq(NotificationType.ORDER_UPDATES)
        );
    }

    @Test
    void shouldNotifyClientViaWebSocketEvenWhenNoNotificationTemplate() {
        Order order = buildOrderWithClient(2L, 9L);

        when(orderRepository.findDetailedById(2L)).thenReturn(Optional.of(order));

        OrderLifecycleEvent event = new OrderLifecycleEvent(2L, OrderStatus.PENDING, OrderStatus.PENDING, "system", null);

        listener.handleOrderLifecycleEvent(event);

        verify(webSocketService).notifyClient(9L, order);
        verify(userDeviceService, never()).findByUser(anyLong());
        verifyNoInteractions(pushNotificationService);
    }

    private Order buildOrderWithClient(Long orderId, Long clientId) {
        Order order = new Order();
        order.setId(orderId);
        Client client = new Client();
        client.setId(clientId);
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        order.setItems(List.of());
        return order;
    }
}
