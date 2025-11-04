package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class DriverFinancialSummaryDto {
    BigDecimal cashOnHand;
    BigDecimal unpaidEarnings;
    BigDecimal outstandingDailyFees;
    BigDecimal depositThreshold;
    boolean depositRequired;
    boolean hasPendingDeposit;
    BigDecimal nextPayoutAmount;
    BigDecimal feesToDeduct;
}
