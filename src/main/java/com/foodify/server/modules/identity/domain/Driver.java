package com.foodify.server.modules.identity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverShift;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Driver extends User {
    private boolean available;
    private String phone;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<DriverShift> shifts;

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

    @Column(
            name = "outstanding_daily_fee_days",
            nullable = false,
            columnDefinition = "integer not null default 0"
    )
    private Integer outstandingDailyFeeDays = 0;

    @Column(name = "last_daily_fee_date")
    private LocalDate lastDailyFeeDate;

    @Column(name = "deposit_warning_sent_at")
    private LocalDateTime depositWarningSentAt;
}
