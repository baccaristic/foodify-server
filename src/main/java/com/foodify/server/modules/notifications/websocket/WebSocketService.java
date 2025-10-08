package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
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
        sendDriverUpdate(driverId, OrderMapper.toDto(order));
    }

    public void notifyDriverUpcoming(Long driverId, Order order) {
        OrderDto dto = OrderMapper.toDto(order);
        if (dto != null) {
            dto.setUpcoming(true);
        }
        sendDriverUpdate(driverId, dto);
    }

    public void notifyClient(Long clientId, Order order) {
        messagingTemplate.convertAndSendToUser(
                clientId.toString(),
                "/queue/orders",
                orderNotificationMapper.toDto(order)
        );
    }

    private void sendDriverUpdate(Long driverId, OrderDto dto) {
        messagingTemplate.convertAndSendToUser(
                driverId.toString(),
                "/queue/orders",
                dto
        );
    }
}
