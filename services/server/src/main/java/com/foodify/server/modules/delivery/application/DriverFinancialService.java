package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.delivery.dto.DriverDepositAdminDto;
import com.foodify.server.modules.delivery.dto.DriverDepositDto;
import com.foodify.server.modules.delivery.dto.DriverFinancialSummaryDto;
import com.foodify.server.modules.delivery.repository.DriverDepositRepository;
import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.AdminRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverFinancialService {
    public static final BigDecimal DEPOSIT_THRESHOLD = new BigDecimal("250.00").setScale(2, RoundingMode.HALF_UP);
    public static final BigDecimal DAILY_FEE = new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.12");

    private final DriverRepository driverRepository;
    private final DriverDepositRepository driverDepositRepository;
    private final AdminRepository adminRepository;
    private final DriverAvailabilityService driverAvailabilityService;

    @Transactional
    public void recordDelivery(Order order) {
        if (order == null) {
            return;
        }
        Delivery delivery = order.getDelivery();
        if (delivery == null || delivery.getDriver() == null || delivery.getDriver().getId() == null) {
            return;
        }

        Long driverId = delivery.getDriver().getId();
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver == null) {
            return;
        }

        BigDecimal itemsTotal = OrderPricingCalculator.calculateTotal(order);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee()).orElse(ZERO);
        BigDecimal orderTotal = Optional.ofNullable(order.getTotal())
                .orElse(itemsTotal.add(deliveryFee)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal commission = itemsTotal.multiply(COMMISSION_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal driverEarnings = commission.add(deliveryFee).setScale(2, RoundingMode.HALF_UP);

        if (isCashOrder(order)) {
            BigDecimal updatedCash = normalize(driver.getCashOnHand()).add(orderTotal).setScale(2, RoundingMode.HALF_UP);
            driver.setCashOnHand(updatedCash);
        }

        BigDecimal updatedEarnings = normalize(driver.getUnpaidEarnings())
                .add(driverEarnings)
                .setScale(2, RoundingMode.HALF_UP);
        driver.setUnpaidEarnings(updatedEarnings);
        driverRepository.save(driver);
    }

    private boolean isCashOrder(Order order) {
        String paymentMethod = Optional.ofNullable(order)
                .map(Order::getPaymentMethod)
                .map(method -> method.toLowerCase(Locale.ROOT))
                .orElse(null);
        if (paymentMethod == null) {
            return true;
        }
        return paymentMethod.contains("cash");
    }

    public boolean isDepositRequired(Long driverId) {
        if (driverId == null) {
            return false;
        }
        return driverRepository.findById(driverId)
                .map(this::isDepositRequired)
                .orElse(false);
    }

    public boolean isDepositRequired(Driver driver) {
        if (driver == null) {
            return false;
        }
        return normalize(driver.getCashOnHand()).compareTo(DEPOSIT_THRESHOLD) >= 0;
    }

    public void assertCanWork(Driver driver) {
        if (isDepositRequired(driver)) {
            throw new IllegalStateException("Deposit required before accepting new orders or starting a shift.");
        }
    }

    @Transactional
    public void applyDailyFeeIfNeeded(Driver driver) {
        if (driver == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate lastFeeDate = driver.getLastDailyFeeDate();
        if (lastFeeDate != null && !lastFeeDate.isBefore(today)) {
            return;
        }
        BigDecimal updatedFees = normalize(driver.getOutstandingDailyFees())
                .add(DAILY_FEE)
                .setScale(2, RoundingMode.HALF_UP);
        driver.setOutstandingDailyFees(updatedFees);
        driver.setLastDailyFeeDate(today);
        driverRepository.save(driver);
    }

    public DriverFinancialSummaryDto getSummary(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
        BigDecimal cashOnHand = normalize(driver.getCashOnHand());
        BigDecimal unpaidEarnings = normalize(driver.getUnpaidEarnings());
        BigDecimal outstandingFees = normalize(driver.getOutstandingDailyFees());
        boolean depositRequired = isDepositRequired(driver);
        boolean hasPending = driverDepositRepository
                .findFirstByDriver_IdAndStatusOrderByCreatedAtDesc(driverId, DriverDepositStatus.PENDING)
                .isPresent();
        BigDecimal feesToDeduct = outstandingFees.min(unpaidEarnings);
        BigDecimal nextPayout = unpaidEarnings.subtract(outstandingFees);
        if (nextPayout.compareTo(BigDecimal.ZERO) < 0) {
            nextPayout = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return DriverFinancialSummaryDto.builder()
                .cashOnHand(cashOnHand)
                .unpaidEarnings(unpaidEarnings)
                .outstandingDailyFees(outstandingFees)
                .depositThreshold(DEPOSIT_THRESHOLD)
                .depositRequired(depositRequired)
                .hasPendingDeposit(hasPending)
                .nextPayoutAmount(nextPayout.setScale(2, RoundingMode.HALF_UP))
                .feesToDeduct(feesToDeduct.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    @Transactional
    public DriverDepositDto requestDeposit(Long driverId, BigDecimal requestedAmount) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        driverDepositRepository.findFirstByDriver_IdAndStatusOrderByCreatedAtDesc(driverId, DriverDepositStatus.PENDING)
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Existing pending deposit must be confirmed first.");
                });

        BigDecimal cashOnHand = normalize(driver.getCashOnHand());
        if (cashOnHand.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No cash available to deposit.");
        }

        BigDecimal amount = requestedAmount != null ? requestedAmount.setScale(2, RoundingMode.HALF_UP) : cashOnHand;
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be positive.");
        }
        if (amount.compareTo(cashOnHand) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit exceeds current cash on hand.");
        }
        if (cashOnHand.compareTo(DEPOSIT_THRESHOLD) >= 0 && amount.compareTo(cashOnHand) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Deposit the full cash on hand when exceeding the threshold.");
        }

        BigDecimal unpaidEarnings = normalize(driver.getUnpaidEarnings());
        BigDecimal outstandingFees = normalize(driver.getOutstandingDailyFees());
        BigDecimal feesDeducted = outstandingFees.min(unpaidEarnings).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netPayout = unpaidEarnings.subtract(outstandingFees);
        if (netPayout.compareTo(BigDecimal.ZERO) < 0) {
            netPayout = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            netPayout = netPayout.setScale(2, RoundingMode.HALF_UP);
        }

        DriverDeposit deposit = new DriverDeposit();
        deposit.setDriver(driver);
        deposit.setDepositAmount(amount);
        deposit.setEarningsPaid(netPayout);
        deposit.setFeesDeducted(feesDeducted);
        deposit.setStatus(DriverDepositStatus.PENDING);
        driverDepositRepository.save(deposit);

        driver.setCashOnHand(cashOnHand.subtract(amount).setScale(2, RoundingMode.HALF_UP));
        driverRepository.save(driver);
        driverAvailabilityService.refreshAvailability(driverId);

        return toDriverDepositDto(deposit);
    }

    @Transactional
    public DriverDepositAdminDto confirmDeposit(Long adminId, Long depositId) {
        DriverDeposit deposit = driverDepositRepository.findById(depositId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deposit not found"));
        if (deposit.getStatus() != DriverDepositStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Deposit already processed.");
        }
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        deposit.setStatus(DriverDepositStatus.CONFIRMED);
        deposit.setConfirmedAt(LocalDateTime.now());
        deposit.setConfirmedBy(admin);

        Driver driver = deposit.getDriver();
        if (driver != null) {
            BigDecimal updatedEarnings = normalize(driver.getUnpaidEarnings())
                    .subtract(normalize(deposit.getEarningsPaid()));
            if (updatedEarnings.compareTo(BigDecimal.ZERO) < 0) {
                updatedEarnings = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
            driver.setUnpaidEarnings(updatedEarnings);

            BigDecimal updatedFees = normalize(driver.getOutstandingDailyFees())
                    .subtract(normalize(deposit.getFeesDeducted()));
            if (updatedFees.compareTo(BigDecimal.ZERO) < 0) {
                updatedFees = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
            driver.setOutstandingDailyFees(updatedFees);
            driverRepository.save(driver);
        }

        driverDepositRepository.save(deposit);
        return toAdminDto(deposit);
    }

    public List<DriverDepositDto> getDriverDeposits(Long driverId) {
        return driverDepositRepository.findByDriver_IdOrderByCreatedAtDesc(driverId)
                .stream()
                .map(this::toDriverDepositDto)
                .collect(Collectors.toList());
    }

    public List<DriverDepositAdminDto> getDepositsForAdmin(DriverDepositStatus status) {
        List<DriverDeposit> deposits = status != null
                ? driverDepositRepository.findAllByStatusOrderByCreatedAtDesc(status)
                : driverDepositRepository.findAllByOrderByCreatedAtDesc();
        return deposits.stream()
                .map(this::toAdminDto)
                .collect(Collectors.toList());
    }

    private DriverDepositDto toDriverDepositDto(DriverDeposit deposit) {
        if (deposit == null) {
            return null;
        }
        return DriverDepositDto.builder()
                .id(deposit.getId())
                .depositAmount(normalize(deposit.getDepositAmount()))
                .earningsPaid(normalize(deposit.getEarningsPaid()))
                .feesDeducted(normalize(deposit.getFeesDeducted()))
                .status(deposit.getStatus())
                .createdAt(deposit.getCreatedAt())
                .confirmedAt(deposit.getConfirmedAt())
                .build();
    }

    private DriverDepositAdminDto toAdminDto(DriverDeposit deposit) {
        if (deposit == null) {
            return null;
        }
        Driver driver = deposit.getDriver();
        return DriverDepositAdminDto.builder()
                .id(deposit.getId())
                .driverId(driver != null ? driver.getId() : null)
                .driverName(driver != null ? driver.getName() : null)
                .driverPhone(driver != null ? driver.getPhone() : null)
                .depositAmount(normalize(deposit.getDepositAmount()))
                .earningsPaid(normalize(deposit.getEarningsPaid()))
                .feesDeducted(normalize(deposit.getFeesDeducted()))
                .status(deposit.getStatus())
                .createdAt(deposit.getCreatedAt())
                .confirmedAt(deposit.getConfirmedAt())
                .build();
    }

    private BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
