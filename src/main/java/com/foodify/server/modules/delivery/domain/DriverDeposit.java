package com.foodify.server.modules.delivery.domain;

import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_deposits")
@Getter
@Setter
public class DriverDeposit {
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by_id")
    private Admin confirmedBy;

    @Column(name = "deposit_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal depositAmount = ZERO;

    @Column(name = "earnings_paid", precision = 19, scale = 2, nullable = false)
    private BigDecimal earningsPaid = ZERO;

    @Column(name = "fees_deducted", precision = 19, scale = 2, nullable = false)
    private BigDecimal feesDeducted = ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverDepositStatus status = DriverDepositStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (depositAmount == null) {
            depositAmount = ZERO;
        }
        if (earningsPaid == null) {
            earningsPaid = ZERO;
        }
        if (feesDeducted == null) {
            feesDeducted = ZERO;
        }
    }
}
