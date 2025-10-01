package com.foodify.server.ws;

import com.foodify.server.enums.OrderStatus;
import com.foodify.server.repository.OrderRepository;
import com.foodify.server.security.JwtService;
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
            String driverIdStr = claims.getSubject();
            Long driverId = Long.valueOf(driverIdStr);
        }
    }

}