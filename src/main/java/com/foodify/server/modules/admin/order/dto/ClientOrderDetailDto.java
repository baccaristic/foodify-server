package com.foodify.server.modules.admin.order.dto;

import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderDetailDto {
    private Long orderId;
    private String restaurantName;
    private OrderStatus status;
    private Long prepTime; // in minutes
    private Long deliveryTime; // in minutes
    private BigDecimal amount;
    private LocalDateTime orderDate;
    
    // Delivery rating
    private Integer deliveryRating; // overall rating (1-5)
    private String deliveryComment;
    
    // Restaurant rating
    private Boolean restaurantRating; // thumbs up/down
    private String restaurantComment;
}
