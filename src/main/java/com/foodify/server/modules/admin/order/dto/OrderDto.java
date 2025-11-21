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
public class OrderDto {
    private Long id;
    private long restaurantId;
    private String restaurantName;
    private long clientId;
    private String clientName;
    private OrderStatus status;
    private BigDecimal amount;
    private LocalDateTime orderDate;
    private Long preparationTime;
}
