package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DriverDepositAdminDto;
import com.foodify.server.modules.admin.driver.repository.AdminAdminRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverDepositRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverRepository;
import com.foodify.server.modules.admin.driver.helpers.DriverFinancialCalculator;
import com.foodify.server.modules.delivery.application.DriverDepositConfirmedEvent;
import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.delivery.dto.DriverDepositPreviewDto;
import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverDepositService {

    private final AdminDriverRepository adminDriverRepository;
    private final AdminDriverDepositRepository adminDriverDepositRepository;
    private final AdminAdminRepository adminRepository;
    private final DriverFinancialCalculator financialCalculator;
    private final ApplicationEventPublisher eventPublisher;

    public Page<DriverDepositAdminDto> getDepositsForAdmin(Long driverId, DriverDepositStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<DriverDeposit> deposits = status != null
                ? adminDriverDepositRepository.findAllByDriverIdAndStatusOrderByCreatedAtDesc(driverId, status, pageable)
                : adminDriverDepositRepository.findAllByDriverIdOrderByCreatedAtDesc(driverId, pageable);
        
        return deposits.map(this::convertToDriverDepositAdminDto);
    }

    @Transactional
    public DriverDepositAdminDto confirmCashDeposit(Long adminId, Long driverId) {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));

        Driver driver = adminDriverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        applyDailyFeeIfNeeded(driver);

        adminDriverDepositRepository.findFirstByDriver_IdAndStatusOrderByCreatedAtDesc(driverId, DriverDepositStatus.PENDING)
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Existing pending deposit must be confirmed first.");
                });

        DepositCalculation calculation = financialCalculator.calculateDeposit(driver);
        DriverDeposit pendingDeposit = createPendingDepositForDriver(driver, calculation);
        
        return confirmDeposit(adminId, pendingDeposit.getId());
    }

    @Transactional
    public DriverDepositPreviewDto previewDeposit(Long driverId) {
        Driver driver = adminDriverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        applyDailyFeeIfNeeded(driver);

        adminDriverDepositRepository.findFirstByDriver_IdAndStatusOrderByCreatedAtDesc(driverId, DriverDepositStatus.PENDING)
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Existing pending deposit must be confirmed first.");
                });

        DepositCalculation calculation = financialCalculator.calculateDeposit(driver);
        return DriverDepositPreviewDto.builder()
                .driverId(driver.getId())
                .driverName(driver.getName())
                .cashOnHand(calculation.getCashOnHand())
                .depositAmount(calculation.getCashOnHand())
                .unpaidEarnings(calculation.getUnpaidEarnings())
                .outstandingDailyFeeDays(calculation.getOutstandingDays())
                .outstandingDailyFees(calculation.getOutstandingFees())
                .feesToDeduct(calculation.getFeesDeducted())
                .payoutAmount(calculation.getNetPayout())
                .build();
    }

    @jakarta.transaction.Transactional
    public void applyDailyFeeIfNeeded(Driver driver) {
        if (driver == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate lastFeeDate = driver.getLastDailyFeeDate();

        LocalDate effectiveLastFeeDate = lastFeeDate != null ? lastFeeDate : today.minusDays(1);
        if (!effectiveLastFeeDate.isBefore(today)) {
            return;
        }

        long daysToCharge = ChronoUnit.DAYS.between(effectiveLastFeeDate, today);
        if (daysToCharge <= 0) {
            return;
        }

        int currentOutstandingDays = Math.max(0, Optional.ofNullable(driver.getOutstandingDailyFeeDays()).orElse(0));
        int updatedDays = Math.toIntExact(daysToCharge) + currentOutstandingDays;
        driver.setOutstandingDailyFeeDays(updatedDays);
        driver.setOutstandingDailyFees(financialCalculator.calculateDailyFeeAmount(updatedDays));
        driver.setLastDailyFeeDate(today);
        adminDriverRepository.save(driver);
    }

    private DriverDeposit createPendingDepositForDriver(Driver driver, DepositCalculation calculation) {
        if (driver == null || driver.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver is required to record a deposit.");
        }

        if (calculation.getCashOnHand().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No cash available to deposit.");
        }

        DriverDeposit deposit = new DriverDeposit();
        deposit.setDriver(driver);
        deposit.setDepositAmount(calculation.getCashOnHand());
        deposit.setEarningsPaid(calculation.getNetPayout());
        deposit.setFeesDeducted(calculation.getFeesDeducted());
        deposit.setStatus(DriverDepositStatus.PENDING);
        adminDriverDepositRepository.save(deposit);

        driver.setCashOnHand(financialCalculator.normalize(null));
        adminDriverRepository.save(driver);

        return deposit;
    }

    @Transactional
    public DriverDepositAdminDto confirmDeposit(Long adminId, Long depositId) {
        DriverDeposit deposit = adminDriverDepositRepository.findById(depositId)
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
            updateDriverFinancesAfterDeposit(driver, deposit);
            adminDriverRepository.save(driver);
            
            if (driver.getId() != null) {
                eventPublisher.publishEvent(new DriverDepositConfirmedEvent(driver.getId()));
            }
        }

        adminDriverDepositRepository.save(deposit);
        return convertToDriverDepositAdminDto(deposit);
    }

    private void updateDriverFinancesAfterDeposit(Driver driver, DriverDeposit deposit) {
        BigDecimal updatedEarnings = financialCalculator.normalize(driver.getUnpaidEarnings())
                .subtract(financialCalculator.normalize(deposit.getEarningsPaid()))
                .max(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        driver.setUnpaidEarnings(updatedEarnings);

        BigDecimal deductedFees = financialCalculator.normalize(deposit.getFeesDeducted());
        int paidDays = deductedFees.divide(DriverFinancialCalculator.DAILY_FEE, 0, RoundingMode.DOWN).intValue();
        int currentOutstandingDays = Math.max(0, Optional.ofNullable(driver.getOutstandingDailyFeeDays()).orElse(0));
        int updatedOutstandingDays = Math.max(0, currentOutstandingDays - paidDays);
        
        driver.setOutstandingDailyFeeDays(updatedOutstandingDays);
        driver.setOutstandingDailyFees(financialCalculator.calculateDailyFeeAmount(updatedOutstandingDays));
        driver.setDepositWarningSentAt(null);
    }

    private DriverDepositAdminDto convertToDriverDepositAdminDto(DriverDeposit deposit) {
        if (deposit == null) {
            return null;
        }
        
        return DriverDepositAdminDto.builder()
                .id(deposit.getId())
                .depositAmount(financialCalculator.normalize(deposit.getDepositAmount()))
                .earningsPaid(financialCalculator.normalize(deposit.getEarningsPaid()))
                .feesDeducted(financialCalculator.normalize(deposit.getFeesDeducted()))
                .status(deposit.getStatus())
                .createdAt(deposit.getCreatedAt())
                .confirmedAt(deposit.getConfirmedAt())
                .build();
    }
}
