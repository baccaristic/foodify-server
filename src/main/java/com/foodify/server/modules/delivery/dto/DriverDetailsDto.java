package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class DriverDetailsDto {
    Long id;
    String name;
    String email;
    String phone;
    Boolean isOnline;
    Double averageRating;
    Long totalOrders;
    Long onTimeOrders;
    Long canceledOrders;
    BigDecimal unpaidEarnings;
    BigDecimal outstandingDailyFees;
    LocalDate lastDailyFeeDate;
    LocalDateTime joinedAt;
}
