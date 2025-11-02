package com.foodify.server.modules.orders.mapper;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.dto.OrderItemDTO;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderItemPricing;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderPricingBreakdown;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
            dto.setRestaurantImage(order.getRestaurant().getImageUrl());
            dto.setRestaurantIcon(order.getRestaurant().getIconUrl());

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
                    .map(OrderMapper::toOrderItemDto)
                    .toList());
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
        dto.setEstimatedReadyAt(order.getEstimatedReadyAt());

        OrderPricingBreakdown pricing = OrderPricingCalculator.calculatePricing(order);

        BigDecimal itemsSubtotal = Optional.ofNullable(order.getItemsSubtotal()).orElse(pricing.itemsSubtotal());
        BigDecimal extrasTotal = Optional.ofNullable(order.getExtrasTotal()).orElse(pricing.extrasTotal());
        BigDecimal promotionDiscount = Optional.ofNullable(order.getPromotionDiscount()).orElse(pricing.promotionDiscount());
        BigDecimal itemsTotal = Optional.ofNullable(order.getItemsTotal()).orElse(pricing.itemsTotal());
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        BigDecimal serviceFee = Optional.ofNullable(order.getServiceFee())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        BigDecimal total = Optional.ofNullable(order.getTotal())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(itemsTotal.add(deliveryFee).add(serviceFee).setScale(2, RoundingMode.HALF_UP));
        BigDecimal tipPercentage = Optional.ofNullable(order.getTipPercentage())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal tipAmount = Optional.ofNullable(order.getTipAmount())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBeforeTip = total.subtract(tipAmount);
        if (totalBeforeTip.compareTo(BigDecimal.ZERO) < 0) {
            totalBeforeTip = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            totalBeforeTip = totalBeforeTip.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal cashToCollect = Optional.ofNullable(order.getCashToCollect())
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(null);

        dto.setItemsSubtotal(itemsSubtotal);
        dto.setExtrasTotal(extrasTotal);
        dto.setPromotionDiscount(promotionDiscount);
        dto.setItemsTotal(itemsTotal);
        dto.setDeliveryFee(deliveryFee);
        dto.setServiceFee(serviceFee);
        dto.setTotal(total);
        dto.setTotalBeforeTip(totalBeforeTip);
        dto.setTipPercentage(tipPercentage);
        dto.setTipAmount(tipAmount);
        dto.setCashToCollect(cashToCollect);

        return dto;
    }

    private static OrderItemDTO toOrderItemDto(OrderItem item) {
        if (item == null || item.getMenuItem() == null) {
            return null;
        }

        OrderItemPricing pricing = resolveItemPricing(item);

        var extras = Optional.ofNullable(item.getMenuItemExtras())
                .orElseGet(java.util.Collections::emptySet)
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

    private static OrderItemPricing resolveItemPricing(OrderItem item) {
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
}

