package com.foodify.server.ws;

import com.foodify.server.models.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyDriver(Long driverId, Order order) {
        messagingTemplate.convertAndSendToUser(
                driverId.toString(),
                "/queue/orders",
                order
        );
    }
}
