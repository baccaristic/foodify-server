package com.foodify.server.modules.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "driver_shift_balances")
@Getter
@Setter
public class DriverShiftBalance {
    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.12");
    private static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "shift_id", nullable = false, unique = true)
    private DriverShift shift;

    @Column(name = "total_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount = ZERO_AMOUNT;

    @Column(name = "driver_share", precision = 19, scale = 2, nullable = false)
    private BigDecimal driverShare = ZERO_AMOUNT;

    @Column(name = "office_share", precision = 19, scale = 2, nullable = false)
    private BigDecimal officeShare = ZERO_AMOUNT;

    @Column(name = "settled", nullable = false)
    private boolean settled = false;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    public void recordOrder(BigDecimal orderTotal) {
        BigDecimal safeTotal = Optional.ofNullable(orderTotal)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal driverCommission = safeTotal.multiply(COMMISSION_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal officePortion = safeTotal.subtract(driverCommission)
                .setScale(2, RoundingMode.HALF_UP);

        totalAmount = totalAmount.add(safeTotal).setScale(2, RoundingMode.HALF_UP);
        driverShare = driverShare.add(driverCommission).setScale(2, RoundingMode.HALF_UP);
        officeShare = officeShare.add(officePortion).setScale(2, RoundingMode.HALF_UP);
        settled = false;
        settledAt = null;
    }

    public void markSettled(LocalDateTime settlementTime) {
        this.settled = true;
        this.settledAt = settlementTime;
    }
}
