package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class DriverDepositPreviewDto {
    Long driverId;
    String driverName;
    BigDecimal cashOnHand;
    BigDecimal depositAmount;
    BigDecimal unpaidEarnings;
    Integer outstandingDailyFeeDays;
    BigDecimal outstandingDailyFees;
    BigDecimal feesToDeduct;
    BigDecimal payoutAmount;
}
