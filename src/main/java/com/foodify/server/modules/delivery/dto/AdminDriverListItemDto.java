package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AdminDriverListItemDto {
    Long id;
    String name;
    String email;
    String phone;
    Boolean isOnline;
    Boolean paymentStatus; // true if payment is up to date
    Double averageRating;
    Long totalOrders;
    BigDecimal unpaidEarnings;
    BigDecimal outstandingDailyFees;
}
