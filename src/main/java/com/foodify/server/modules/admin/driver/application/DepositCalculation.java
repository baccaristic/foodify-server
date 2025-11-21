package com.foodify.server.modules.admin.driver.application;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DepositCalculation {
    private final BigDecimal cashOnHand;
    private final BigDecimal unpaidEarnings;
    private final int outstandingDays;
    private final BigDecimal outstandingFees;
    private final BigDecimal feesDeducted;
    private final BigDecimal netPayout;
}
