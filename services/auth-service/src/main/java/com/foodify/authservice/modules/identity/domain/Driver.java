package com.foodify.authservice.modules.identity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@Entity
public class Driver extends User {
    private boolean available;
    private String phone;

    @Column(
            name = "cash_on_hand",
            precision = 19,
            scale = 2,
            nullable = false,
            columnDefinition = "numeric(19,2) not null default 0"
    )
    private BigDecimal cashOnHand = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Column(
            name = "unpaid_earnings",
            precision = 19,
            scale = 2,
            nullable = false,
            columnDefinition = "numeric(19,2) not null default 0"
    )
    private BigDecimal unpaidEarnings = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Column(
            name = "outstanding_daily_fees",
            precision = 19,
            scale = 2,
            nullable = false,
            columnDefinition = "numeric(19,2) not null default 0"
    )
    private BigDecimal outstandingDailyFees = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Column(name = "last_daily_fee_date")
    private LocalDate lastDailyFeeDate;
}
