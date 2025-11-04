package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class PaymentHistoryDto {
    Long id;
    BigDecimal amount;
    String paymentMethod;
    String notes;
    LocalDateTime createdAt;
    String confirmedByAdminName;
}
