package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatusHistory;
import com.foodify.server.modules.orders.dto.ClientSummaryDTO;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class OrderNotificationMapper {

    public OrderNotificationDTO toDto(Order order) {
        LocationDto deliveryLocation = resolveDeliveryLocation(order);
        OrderNotificationDTO.RestaurantSummary restaurantSummary = toRestaurantSummary(order.getRestaurant());
        OrderNotificationDTO.DeliverySummary deliverySummary = toDeliverySummary(order.getDelivery());

        List<OrderItemDTO> items = order.getItems() == null ? List.of() : order.getItems().stream().map(item -> new OrderItemDTO(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getMenuItemExtras().stream().map(MenuItemExtra::getName).toList(),
                item.getSpecialInstructions()
        )).toList();

        return new OrderNotificationDTO(
                order.getId(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getDate(),
                items,
                SavedAddressSummaryMapper.from(order.getSavedAddress()),
                new ClientSummaryDTO(
                        order.getClient().getId(),
                        order.getClient().getName()
                ),
                order.getStatus(),
                deliveryLocation,
                restaurantSummary,
                deliverySummary,
                buildStatusHistory(order)
        );
    }

    private LocationDto resolveDeliveryLocation(Order order) {
        LocationDto fromSavedAddress = SavedAddressSummaryMapper.toLocation(order.getSavedAddress());
        if (fromSavedAddress != null) {
            return fromSavedAddress;
        }

        double lat = order.getLat();
        double lng = order.getLng();
        if (lat == 0.0 && lng == 0.0) {
            return null;
        }
        return new LocationDto(lat, lng);
    }

    private OrderNotificationDTO.RestaurantSummary toRestaurantSummary(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        LocationDto location = null;
        double latitude = restaurant.getLatitude();
        double longitude = restaurant.getLongitude();
        if (!(latitude == 0.0 && longitude == 0.0)) {
            location = new LocationDto(latitude, longitude);
        }
        return new OrderNotificationDTO.RestaurantSummary(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getImageUrl(),
                location
        );
    }

    private OrderNotificationDTO.DeliverySummary toDeliverySummary(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        Driver driver = delivery.getDriver();
        OrderNotificationDTO.DriverSummary driverSummary = null;
        if (driver != null) {
            driverSummary = new OrderNotificationDTO.DriverSummary(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhone()
            );
        }

        return new OrderNotificationDTO.DeliverySummary(
                delivery.getId(),
                driverSummary,
                delivery.getTimeToPickUp(),
                delivery.getDeliveryTime(),
                delivery.getPickupTime(),
                delivery.getDeliveredTime()
        );
    }

    private List<OrderNotificationDTO.OrderStatusHistoryDTO> buildStatusHistory(Order order) {
        if (order.getStatusHistory() == null) {
            return List.of();
        }

        return order.getStatusHistory().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(OrderStatusHistory::getChangedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(history -> new OrderNotificationDTO.OrderStatusHistoryDTO(
                        history.getAction(),
                        history.getPreviousStatus(),
                        history.getNewStatus(),
                        history.getChangedBy(),
                        history.getReason(),
                        history.getMetadata(),
                        history.getChangedAt()
                ))
                .toList();
    }
}
