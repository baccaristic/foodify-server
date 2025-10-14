package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderPricingBreakdown;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

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
        Delivery delivery = order.getDelivery();
        if (delivery != null) {
            if (delivery.getDriver() != null) {
                Driver driver = delivery.getDriver();
                dto.setDriverId(driver.getId());
                dto.setDriverName(driver.getName());
                dto.setDriverPhone(driver.getPhone());
            }

            dto.setEstimatedPickUpTime(delivery.getTimeToPickUp());
            dto.setEstimatedDeliveryTime(delivery.getDeliveryTime());
            dto.setDriverAssignedAt(delivery.getAssignedTime());
            dto.setPickedUpAt(delivery.getPickupTime());
            dto.setDeliveredAt(delivery.getDeliveredTime());
            dto.setUpcoming(false);
        } else if (order.getPendingDriver() != null) {
            dto.setDriverId(order.getPendingDriver().getId());
            dto.setDriverName(order.getPendingDriver().getName());
            dto.setDriverPhone(order.getPendingDriver().getPhone());
            dto.setUpcoming(true);
        } else {
            dto.setUpcoming(false);
        }

        // Other fields
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getOrderTime());

        OrderPricingBreakdown pricing = OrderPricingCalculator.calculatePricing(order);

        BigDecimal itemsSubtotal = Optional.ofNullable(order.getItemsSubtotal()).orElse(pricing.itemsSubtotal());
        BigDecimal extrasTotal = Optional.ofNullable(order.getExtrasTotal()).orElse(pricing.extrasTotal());
        BigDecimal promotionDiscount = Optional.ofNullable(order.getPromotionDiscount()).orElse(pricing.promotionDiscount());
        BigDecimal itemsTotal = Optional.ofNullable(order.getItemsTotal()).orElse(pricing.itemsTotal());
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        BigDecimal total = Optional.ofNullable(order.getTotal())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(itemsTotal.add(deliveryFee).setScale(2, RoundingMode.HALF_UP));

        dto.setItemsSubtotal(itemsSubtotal);
        dto.setExtrasTotal(extrasTotal);
        dto.setPromotionDiscount(promotionDiscount);
        dto.setItemsTotal(itemsTotal);
        dto.setDeliveryFee(deliveryFee);
        dto.setTotal(total);

        return dto;
    }
}
