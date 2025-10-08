package com.foodify.server.modules.orders.dto;

import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.Data;

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

    private Long clientId;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private LocationDto clientLocation;
    private SavedAddressSummaryDto savedAddress;

    private Long total;
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
