package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final OrderNotificationMapper orderNotificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyDriver(Long driverId, Order order) {
        messagingTemplate.convertAndSendToUser(
                driverId.toString(),
                "/queue/orders",
                OrderMapper.toDto(order)
        );
    }

    public void notifyClient(Long clientId, Order order) {
        messagingTemplate.convertAndSendToUser(
                clientId.toString(),
                "/queue/orders",
                orderNotificationMapper.toDto(order)
        );
    }
}
