package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.orders.dto.ClientSummaryDTO;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationMapper {

    public OrderNotificationDTO toDto(Order order) {
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
                SavedAddressSummaryMapper.from(order.getSavedAddress()),
                new ClientSummaryDTO(
                        order.getClient().getId(),
                        order.getClient().getName()
                )
        );
    }
}
