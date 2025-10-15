package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final OrderRepository orderRepository;
    private final WebSocketService webSocketService;
    private final JwtService jwtService;

    private static final List<OrderStatus> RESTAURANT_ACTIVE_STATUSES = List.of(
            OrderStatus.PENDING,
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // Try direct native headers first (works in ChannelInterceptor on CONNECT)
        List<String> authHeaders = accessor.getNativeHeader("Authorization");

        if ((authHeaders == null || authHeaders.isEmpty())) {
            // Fallback: pull from simpConnectMessage
            Message<?> connectMessage = (Message<?>) accessor.getHeader("simpConnectMessage");
            if (connectMessage != null) {
                StompHeaderAccessor connectAccessor = StompHeaderAccessor.wrap(connectMessage);
                authHeaders = connectAccessor.getNativeHeader("Authorization");
            }
        }

        if (authHeaders != null && !authHeaders.isEmpty()) {
            String token = authHeaders.get(0).replace("Bearer ", "");
            Claims claims = jwtService.parseAccessToken(token);
            handleAuthenticatedConnection(claims);
        }
    }

    private void handleAuthenticatedConnection(Claims claims) {
        if (claims == null) {
            return;
        }

        String userIdStr = claims.getSubject();
        if (userIdStr == null) {
            return;
        }

        Long userId;
        try {
            userId = Long.valueOf(userIdStr);
        } catch (NumberFormatException ex) {
            return;
        }

        Object rawRole = claims.get("role");
        if (rawRole == null) {
            return;
        }

        String roleValue = rawRole.toString();
        if (Role.RESTAURANT_ADMIN.name().equals(roleValue)) {
            List<Order> activeOrders = orderRepository
                    .findAllByRestaurant_Admin_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(userId, RESTAURANT_ACTIVE_STATUSES);
            webSocketService.sendRestaurantSnapshot(userId, activeOrders);
        }
    }
}
