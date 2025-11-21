package com.foodify.server.modules.admin.driver.helpers;

import com.foodify.server.modules.admin.driver.application.DepositCalculation;
import com.foodify.server.modules.identity.domain.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverFinancialCalculator {

    public static final BigDecimal DAILY_FEE = new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public DepositCalculation calculateDeposit(Driver driver) {
        if (driver == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver is required to record a deposit.");
        }

        BigDecimal cashOnHand = normalize(driver.getCashOnHand());
        if (cashOnHand.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No cash available to deposit.");
        }

        BigDecimal unpaidEarnings = normalize(driver.getUnpaidEarnings());
        int outstandingDays = Math.max(0, Optional.ofNullable(driver.getOutstandingDailyFeeDays()).orElse(0));
        BigDecimal outstandingFees = calculateDailyFeeAmount(outstandingDays);
        BigDecimal feesDeducted = calculateFeesToDeduct(unpaidEarnings, outstandingDays);
        BigDecimal netPayout = unpaidEarnings.subtract(feesDeducted).max(ZERO);

        return DepositCalculation.builder()
                .cashOnHand(cashOnHand)
                .unpaidEarnings(unpaidEarnings)
                .outstandingDays(outstandingDays)
                .outstandingFees(outstandingFees)
                .feesDeducted(feesDeducted)
                .netPayout(netPayout)
                .build();
    }

    public BigDecimal calculateDailyFeeAmount(int days) {
        if (days <= 0) {
            return ZERO;
        }
        return DAILY_FEE.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateFeesToDeduct(BigDecimal unpaidEarnings, int outstandingDays) {
        BigDecimal normalizedEarnings = normalize(unpaidEarnings);
        if (outstandingDays <= 0 || normalizedEarnings.compareTo(BigDecimal.ZERO) <= 0) {
            return ZERO;
        }

        BigDecimal maxDaysFromEarnings = normalizedEarnings.divideToIntegralValue(DAILY_FEE);
        int payableDays = Math.min(outstandingDays, maxDaysFromEarnings.intValue());

        if (payableDays <= 0) {
            return ZERO;
        }

        return calculateDailyFeeAmount(payableDays);
    }

    public BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isDriverPaid(Driver driver) {
        BigDecimal zero = BigDecimal.ZERO;

        boolean noCashOnHand = driver.getCashOnHand() == null ||
                driver.getCashOnHand().compareTo(zero) <= 0;

        boolean noUnpaidEarnings = driver.getUnpaidEarnings() == null ||
                driver.getUnpaidEarnings().compareTo(zero) <= 0;

        boolean noOutstandingFees = driver.getOutstandingDailyFees() == null ||
                driver.getOutstandingDailyFees().compareTo(zero) <= 0;

        return noCashOnHand && noUnpaidEarnings && noOutstandingFees;
    }
}
