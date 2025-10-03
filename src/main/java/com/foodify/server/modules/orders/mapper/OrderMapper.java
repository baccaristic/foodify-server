package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.orders.domain.Order;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());

        // Restaurant
        if (order.getRestaurant() != null) {
            dto.setRestaurantId(order.getRestaurant().getId());
            dto.setRestaurantName(order.getRestaurant().getName());
            dto.setRestaurantAddress(order.getRestaurant().getAddress());
            dto.setRestaurantPhone(order.getRestaurant().getPhone());

            dto.setRestaurantLocation(new LocationDto(
                    order.getRestaurant().getLatitude(),
                    order.getRestaurant().getLongitude()
            ));
        }

        // Client
        if (order.getClient() != null) {
            dto.setClientId(order.getClient().getId());
            dto.setClientName(order.getClient().getName());
            dto.setClientPhone(order.getClient().getPhoneNumber());
            dto.setClientAddress(order.getDeliveryAddress());
            dto.setClientLocation(new LocationDto(order.getLat(), order.getLng()));
        }

        dto.setSavedAddress(SavedAddressSummaryMapper.from(order.getSavedAddress()));

        // Order Items
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(item -> new OrderItemDTO(
                            item.getMenuItem().getId(),
                            item.getMenuItem().getName(),
                            item.getQuantity(),
                            item.getMenuItemExtras().stream().map(extra -> extra.getName()).toList(),
                            item.getSpecialInstructions()
                    ))
                    .toList()
            );
        }

        // Delivery / Driver
        if (order.getDelivery() != null && order.getDelivery().getDriver() != null) {
            Driver driver = order.getDelivery().getDriver();
            dto.setDriverId(driver.getId());
            dto.setDriverName(driver.getName());
            dto.setDriverPhone(driver.getPhone());

            dto.setEstimatedPickUpTime(order.getDelivery().getTimeToPickUp());
            dto.setEstimatedDeliveryTime(order.getDelivery().getDeliveryTime());
        } else if (order.getPendingDriver() != null) {
            dto.setDriverId(order.getPendingDriver().getId());
            dto.setDriverName(order.getPendingDriver().getName());
            dto.setDriverPhone(order.getPendingDriver().getPhone());
        }

        // Other fields
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getOrderTime());
        dto.setTotal(calculateTotal(order)); // Implement based on items/payment

        return dto;
    }

    private static Long calculateTotal(Order order) {
        if (order.getItems() == null) return 0L;

        double total = 0;
        for (var item : order.getItems()) {
            double price = item.getMenuItem().getPrice();
            total += price * item.getQuantity();
            if (item.getMenuItemExtras() != null) {
                total += item.getMenuItemExtras().stream().mapToDouble(MenuItemExtra::getPrice).sum();
            }
        }
        return (long) total;
    }
}
