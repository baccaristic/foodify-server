package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminShiftDeliveryDto {
    private Long id;
    private Long orderId;
    private String restaurantName;
    private String deliveryAddress;
    private Long deliveryTime;
    private Long timeToPickUp;
    private LocalDateTime assignedTime;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveredTime;
    private Double rating;
    private String ratingComment;
}
