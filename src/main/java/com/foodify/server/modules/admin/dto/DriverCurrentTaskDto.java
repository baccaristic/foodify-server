package com.foodify.server.modules.admin.dto;

import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DriverCurrentTaskDto {
    Long orderId;
    OrderStatus status;
    String restaurantName;
    String deliveryAddress;
    LocalDateTime estimatedDeliveryTime;
    Double restaurantLatitude;
    Double restaurantLongitude;
    Double deliveryLatitude;
    Double deliveryLongitude;
}
