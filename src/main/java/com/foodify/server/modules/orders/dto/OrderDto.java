package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String restaurantName;
    private Long restaurantId;
    private String restaurantAddress;
    private LocationDto restaurantLocation;
    private String restaurantPhone;
    private String restaurantImage;

    private Long clientId;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private LocationDto clientLocation;
    private SavedAddressSummaryDto savedAddress;

    private BigDecimal itemsSubtotal;
    private BigDecimal extrasTotal;
    private BigDecimal promotionDiscount;
    private BigDecimal itemsTotal;
    private BigDecimal deliveryFee;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items = new ArrayList<>();

    private Long driverId;
    private String driverName;
    private String driverPhone;

    private Long estimatedPickUpTime;
    private Long estimatedDeliveryTime;

    private LocalDateTime driverAssignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;

    private boolean upcoming;
}
