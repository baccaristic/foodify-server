package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.dto.ClientSummaryDTO;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.dto.SavedAddressSummaryDto;
import com.foodify.server.modules.orders.domain.OrderStatusHistory;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderItemPricing;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderPricingBreakdown;
import com.foodify.server.modules.orders.repository.OrderStatusHistoryRepository;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OrderNotificationMapper {

    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final DriverLocationService driverLocationService;

    public OrderNotificationMapper(OrderStatusHistoryRepository statusHistoryRepository,
                                   DriverLocationService driverLocationService) {
        this.statusHistoryRepository = statusHistoryRepository;
        this.driverLocationService = driverLocationService;
    }

    public OrderNotificationDTO toDto(Order order) {
        return toDto(order, null, null);
    }

    public OrderNotificationDTO toClientDto(Order order) {
        return toDto(order, order != null ? order.getDeliveryToken() : null, null);
    }

    public OrderNotificationDTO toRestaurantDto(Order order) {
        return toDto(order, null, order != null ? order.getPickupToken() : null);
    }

    private OrderNotificationDTO toDto(Order order, String deliveryToken, String pickupToken) {
        if (order == null) {
            return null;
        }
        LocationDto deliveryLocation = resolveDeliveryLocation(order);
        OrderNotificationDTO.RestaurantSummary restaurantSummary = toRestaurantSummary(order.getRestaurant());
        OrderNotificationDTO.DeliverySummary deliverySummary = toDeliverySummary(order.getDelivery());
        String restaurantImage = order.getRestaurant() != null ? order.getRestaurant().getImageUrl() : null;
        String restaurantIcon = order.getRestaurant() != null ? order.getRestaurant().getIconUrl() : null;

        List<OrderItemDTO> items = order.getItems() == null ? List.of() : order.getItems().stream()
                .map(this::toOrderItemDto)
                .filter(Objects::nonNull)
                .toList();

        OrderPricingBreakdown pricing = OrderPricingCalculator.calculatePricing(order);
        BigDecimal itemsSubtotal = Optional.ofNullable(order.getItemsSubtotal()).orElse(pricing.itemsSubtotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal extrasTotal = Optional.ofNullable(order.getExtrasTotal()).orElse(pricing.extrasTotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal promotionDiscount = Optional.ofNullable(order.getPromotionDiscount()).orElse(pricing.promotionDiscount())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsTotal = Optional.ofNullable(order.getItemsTotal()).orElse(pricing.itemsTotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal orderTotal = Optional.ofNullable(order.getTotal())
                .orElse(itemsTotal.add(deliveryFee))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal promotionalSubtotal = itemsSubtotal.subtract(promotionDiscount);
        if (promotionalSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            promotionalSubtotal = BigDecimal.ZERO;
        }
        promotionalSubtotal = promotionalSubtotal.setScale(2, RoundingMode.HALF_UP);

        OrderNotificationDTO.PaymentSummary paymentSummary = new OrderNotificationDTO.PaymentSummary(
                promotionalSubtotal,
                extrasTotal,
                orderTotal,
                itemsSubtotal,
                promotionDiscount,
                itemsTotal,
                deliveryFee
        );

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
                restaurantImage,
                restaurantIcon,
                restaurantSummary,
                deliverySummary,
                paymentSummary,
                buildStatusHistory(order),
                deliveryToken,
                pickupToken
        );
    }

    private OrderItemDTO toOrderItemDto(OrderItem item) {
        if (item == null || item.getMenuItem() == null) {
            return null;
        }

        OrderItemPricing pricing = resolveItemPricing(item);

        List<String> extras = Optional.ofNullable(item.getMenuItemExtras())
                .orElseGet(List::of)
                .stream()
                .map(MenuItemExtra::getName)
                .toList();

        return new OrderItemDTO(
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                extras,
                item.getSpecialInstructions(),
                pricing.unitBasePrice(),
                pricing.unitPrice(),
                pricing.unitExtrasPrice(),
                pricing.lineSubtotal(),
                pricing.promotionDiscount(),
                pricing.lineItemsTotal(),
                pricing.extrasTotal(),
                pricing.lineTotal()
        );
    }

    private OrderItemPricing resolveItemPricing(OrderItem item) {
        if (item == null) {
            return OrderItemPricing.empty();
        }

        BigDecimal unitBasePrice = item.getUnitBasePrice();
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal unitExtrasPrice = item.getUnitExtrasPrice();
        BigDecimal lineSubtotal = item.getLineSubtotal();
        BigDecimal lineItemsTotal = item.getLineItemsTotal();
        BigDecimal extrasTotal = item.getExtrasTotal();
        BigDecimal promotionDiscount = item.getPromotionDiscount();
        BigDecimal lineTotal = item.getLineTotal();

        if (unitBasePrice != null && unitPrice != null && unitExtrasPrice != null
                && lineSubtotal != null && lineItemsTotal != null && extrasTotal != null
                && promotionDiscount != null && lineTotal != null) {
            return new OrderItemPricing(
                    unitBasePrice.setScale(2, RoundingMode.HALF_UP),
                    unitPrice.setScale(2, RoundingMode.HALF_UP),
                    unitExtrasPrice.setScale(2, RoundingMode.HALF_UP),
                    lineSubtotal.setScale(2, RoundingMode.HALF_UP),
                    lineItemsTotal.setScale(2, RoundingMode.HALF_UP),
                    extrasTotal.setScale(2, RoundingMode.HALF_UP),
                    promotionDiscount.setScale(2, RoundingMode.HALF_UP),
                    lineTotal.setScale(2, RoundingMode.HALF_UP)
            );
        }

        return OrderPricingCalculator.calculateItemPricing(item);
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
                restaurant.getIconUrl(),
                location
        );
    }

    private OrderNotificationDTO.DeliverySummary toDeliverySummary(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        Driver driver = delivery.getDriver();
        OrderNotificationDTO.DriverSummary driverSummary = null;
        LocationDto driverLocation = null;
        if (driver != null) {
            driverSummary = new OrderNotificationDTO.DriverSummary(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhone()
            );

            var position = driverLocationService.getLastKnownPosition(driver.getId());
            if (position != null) {
                driverLocation = new LocationDto(position.getY(), position.getX());
            }
        }

        Order order = delivery.getOrder();
        String address = null;
        LocationDto deliveryLocation = null;
        SavedAddressSummaryDto savedAddress = null;
        if (order != null) {
            address = order.getDeliveryAddress();
            deliveryLocation = resolveDeliveryLocation(order);
            savedAddress = SavedAddressSummaryMapper.from(order.getSavedAddress());
        }

        return new OrderNotificationDTO.DeliverySummary(
                delivery.getId(),
                driverSummary,
                delivery.getTimeToPickUp(),
                delivery.getDeliveryTime(),
                delivery.getPickupTime(),
                delivery.getDeliveredTime(),
                driverLocation,
                address,
                deliveryLocation,
                savedAddress
        );
    }

    private List<OrderNotificationDTO.OrderStatusHistoryDTO> buildStatusHistory(Order order) {
        if (order == null || order.getId() == null) {
            return List.of();
        }

        List<OrderStatusHistory> historyEntries = statusHistoryRepository.findAllByOrderIdOrderByChangedAtAsc(order.getId());
        if (historyEntries.isEmpty()) {
            return List.of();
        }

        return historyEntries.stream()
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
