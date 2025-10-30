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
    private static final BigDecimal DEFAULT_DRIVER_COMMISSION_RATE = new BigDecimal("0.12");
    private static final BigDecimal DEFAULT_TOTAL_COMMISSION_RATE = new BigDecimal("0.17");
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

    @Column(name = "restaurant_share", precision = 19, scale = 2, nullable = false)
    private BigDecimal restaurantShare = ZERO_AMOUNT;

    @Column(name = "settled", nullable = false)
    private boolean settled = false;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    public void recordOrder(BigDecimal itemsTotal, BigDecimal commissionRate, BigDecimal deliveryFee) {
        BigDecimal safeItemsTotal = Optional.ofNullable(itemsTotal)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal safeDeliveryFee = Optional.ofNullable(deliveryFee)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal safeTotal = safeItemsTotal.add(safeDeliveryFee).setScale(2, RoundingMode.HALF_UP);
        BigDecimal normalizedCommission = normalizeCommissionRate(commissionRate);
        BigDecimal driverCommission = safeItemsTotal.multiply(DEFAULT_DRIVER_COMMISSION_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformRate = normalizedCommission.subtract(DEFAULT_DRIVER_COMMISSION_RATE);
        if (platformRate.compareTo(BigDecimal.ZERO) < 0) {
            platformRate = BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
        BigDecimal platformCommission = safeItemsTotal.multiply(platformRate)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal driverTotal = driverCommission.add(safeDeliveryFee)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal restaurantPortion = safeItemsTotal.subtract(driverCommission)
                .subtract(platformCommission)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);

        totalAmount = totalAmount.add(safeTotal).setScale(2, RoundingMode.HALF_UP);
        driverShare = driverShare.add(driverTotal).setScale(2, RoundingMode.HALF_UP);
        restaurantShare = restaurantShare.add(restaurantPortion).setScale(2, RoundingMode.HALF_UP);
        settled = false;
        settledAt = null;
    }

    private BigDecimal normalizeCommissionRate(BigDecimal commissionRate) {
        BigDecimal normalized = Optional.ofNullable(commissionRate)
                .orElse(DEFAULT_TOTAL_COMMISSION_RATE)
                .setScale(4, RoundingMode.HALF_UP);
        if (normalized.compareTo(DEFAULT_DRIVER_COMMISSION_RATE) < 0) {
            return DEFAULT_DRIVER_COMMISSION_RATE.setScale(4, RoundingMode.HALF_UP);
        }
        if (normalized.compareTo(BigDecimal.ONE) > 0) {
            return BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP);
        }
        return normalized;
    }

    public void markSettled(LocalDateTime settlementTime) {
        this.settled = true;
        this.settledAt = settlementTime;
    }
}
