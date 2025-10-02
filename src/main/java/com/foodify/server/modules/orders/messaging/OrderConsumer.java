package com.foodify.server.modules.orders.messaging;

import com.foodify.server.modules.orders.dto.ClientSummaryDTO;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.orders.dto.OrderItemRequest;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "orders", groupId = "order_group")
    @Transactional
    public void handleOrder(OrderRequest request) {
        Order order = new Order();
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setLat(request.getLocation().getLat());
        order.setLng(request.getLocation().getLng());
        order.setPaymentMethod(request.getPaymentMethod());

        order.setClient(clientRepository.findById(request.getUserId()).orElse(null));
        order.setItems(new ArrayList<>());

        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setMenuItem(menuItemRepository.findById(itemReq.getMenuItemId()).orElse(null));
            item.setQuantity(itemReq.getQuantity());
            item.setSpecialInstructions(itemReq.getSpecialInstructions());
            item.setOrder(order);

            // Load extras by IDs
            if (itemReq.getExtraIds() != null && !itemReq.getExtraIds().isEmpty()) {
                List<MenuItemExtra> extras = menuItemExtraRepository.findAllById(itemReq.getExtraIds());
                item.setMenuItemExtras(extras);
            } else {
                item.setMenuItemExtras(Collections.emptyList());
            }

            order.getItems().add(item);
        }

        order.setStatus(OrderStatus.PENDING);

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        order.setRestaurant(restaurant);
        order.setDate(LocalDateTime.now());

        order = orderRepository.save(order);

        messagingTemplate.convertAndSend(
                "/topic/orders/" + restaurant.getAdmin().getId(),
                mapToNotificationDTO(order)
        );
    }


    public OrderNotificationDTO mapToNotificationDTO(Order order) {
        return new OrderNotificationDTO(
                order.getId(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getDate(),
                order.getItems().stream().map(item -> new OrderItemDTO(
                        item.getMenuItem().getId(),
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getMenuItemExtras().stream().map(MenuItemExtra::getName).toList(),
                        item.getSpecialInstructions()
                )).toList(),
                new ClientSummaryDTO(
                        order.getClient().getId(),
                        order.getClient().getName()
                )
        );
    }
}
