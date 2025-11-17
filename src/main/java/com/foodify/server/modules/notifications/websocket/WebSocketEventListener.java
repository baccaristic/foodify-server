package com.foodify.server.modules.notifications.websocket;

import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.identity.repository.RestaurantCashierRepository;
import com.foodify.server.config.OrderViewProperties;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.orders.support.OrderStatusGroups;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
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
    private final OrderViewProperties orderViewProperties;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantCashierRepository restaurantCashierRepository;

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
        if (Role.RESTAURANT_ADMIN.name().equals(roleValue) || Role.RESTAURANT_CASHIER.name().equals(roleValue)) {
            Restaurant restaurant = null;
            
            // Try to get restaurant from admin
            if (Role.RESTAURANT_ADMIN.name().equals(roleValue)) {
                RestaurantAdmin admin = restaurantAdminRepository.findById(userId).orElse(null);
                if (admin != null) {
                    restaurant = admin.getRestaurant();
                }
            } 
            // Try to get restaurant from cashier
            else if (Role.RESTAURANT_CASHIER.name().equals(roleValue)) {
                RestaurantCashier cashier = restaurantCashierRepository.findById(userId).orElse(null);
                if (cashier != null) {
                    restaurant = cashier.getRestaurant();
                }
            }
            
            if (restaurant != null && restaurant.getId() != null) {
                int limit = Math.max(orderViewProperties.getRestaurantSnapshotLimit(), 1);
                List<Order> activeOrders = orderRepository
                        .findAllByRestaurant_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(
                                restaurant.getId(),
                                OrderStatusGroups.RESTAURANT_ACTIVE_STATUSES,
                                PageRequest.of(0, limit)
                        )
                        .getContent();
                webSocketService.sendRestaurantSnapshot(restaurant.getId(), activeOrders);
            }
        }
    }
}
