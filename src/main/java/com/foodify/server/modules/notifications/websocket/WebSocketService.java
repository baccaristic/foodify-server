package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void notifyRestaurant(Long adminId, Order order) {
        messagingTemplate.convertAndSendToUser(
                adminId.toString(),
                "/queue/restaurant/orders",
                orderNotificationMapper.toDto(order)
        );
    }

    public void sendRestaurantSnapshot(Long adminId, List<Order> orders) {
        List<Order> safeOrders = orders == null ? List.of() : orders;
        messagingTemplate.convertAndSendToUser(
                adminId.toString(),
                "/queue/restaurant/orders/snapshot",
                safeOrders.stream()
                        .map(orderNotificationMapper::toDto)
                        .toList()
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
