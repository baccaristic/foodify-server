package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class DailySubscriptionDueDateDto {
    LocalDate lastPaymentDate;
    LocalDate nextDueDate;
    Integer daysPastDue;
    BigDecimal amountDue;
}
