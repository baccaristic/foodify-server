package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.payments.points.dto.PointsPaymentResponse;
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
                orderNotificationMapper.toClientDto(order)
        );
    }

    public void notifyRestaurant(Long restaurantId, Order order) {
        messagingTemplate.convertAndSendToUser(
                restaurantId.toString(),
                "/queue/restaurant/orders",
                orderNotificationMapper.toRestaurantDto(order)
        );
    }

    public void notifyRestaurantNewOrder(Long restaurantId, Order order) {
        messagingTemplate.convertAndSendToUser(
                restaurantId.toString(),
                "/queue/restaurant/orders/new",
                orderNotificationMapper.toRestaurantDto(order)
        );
    }

    public void sendRestaurantSnapshot(Long restaurantId, List<Order> orders) {
        List<Order> safeOrders = orders == null ? List.of() : orders;
        messagingTemplate.convertAndSendToUser(
                restaurantId.toString(),
                "/queue/restaurant/orders/snapshot",
                safeOrders.stream()
                        .map(orderNotificationMapper::toRestaurantDto)
                        .toList()
        );
    }

    public void notifyRestaurantPointsPayment(Long restaurantId, PointsPaymentResponse payment) {
        messagingTemplate.convertAndSendToUser(
                restaurantId.toString(),
                "/queue/restaurant/payments/points",
                payment
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
