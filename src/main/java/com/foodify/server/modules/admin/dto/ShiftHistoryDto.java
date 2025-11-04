package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ShiftHistoryDto {
    Long shiftId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal totalEarnings;
    Integer totalOrders;
    List<ShiftOrderOverviewDto> orders;

    @Value
    @Builder
    public static class ShiftOrderOverviewDto {
        Long orderId;
        String restaurantName;
        String status;
        BigDecimal earnings;
        LocalDateTime deliveryTime;
    }
}
