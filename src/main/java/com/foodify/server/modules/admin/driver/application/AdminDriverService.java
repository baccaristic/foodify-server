package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.*;
import com.foodify.server.modules.admin.driver.repository.*;
import com.foodify.server.modules.delivery.application.DriverDepositConfirmedEvent;
import com.foodify.server.modules.delivery.application.DriverFinancialService;
import com.foodify.server.modules.delivery.domain.*;
import com.foodify.server.modules.delivery.dto.DriverDepositPreviewDto;
import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDriverService {

    private final AdminDriverRepository adminDriverRepository;
    private final AdminDeliveryRepository adminDeliveryRepository;
    private final AdminDeliveryRatingRepository adminDeliveryRatingRepository;
    private final AdminDriverDepositRepository adminDriverDepositRepository;
    private final AdminShiftRepository adminShiftRepository;
    private final AdminAdminRepository adminRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private static final double DEFAULT_DRIVER_COMMISSION_RATE = 0.12;
    public static final BigDecimal DAILY_FEE = new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP);

    /**
     * Get paginated list of drivers with filters
     */
    @Transactional(readOnly = true)
    public Page<DriverListItemDto> getDriverList(String query, boolean paid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driversPage = adminDriverRepository.findAll(
                DriverSpecifications.withFilters(query, paid),
                pageable
        );

        return driversPage.map(driver -> new DriverListItemDto(
                driver.getId(),
                driver.getName(),
                driver.getPhone(),
                isDriverPaid(driver)
        ));
    }

    /**
     * Get driver rating for a specific period
     */
    @Transactional(readOnly = true)
    public Double getDriverRating(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        return adminDeliveryRepository.getDriverRating(driverId, startDate, endDate);
    }

    /**
     * Get driver on-time percentage for a specific period
     */
    @Transactional(readOnly = true)
    public Double getDriverOnTimePercentage(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        return adminDeliveryRepository.getDriverOnTimePercentage(driverId, startDate, endDate);
    }

    /**
     * Get driver average delivery time in minutes
     */
    @Transactional(readOnly = true)
    public Double getDriverAverageDeliveryTime(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        return adminDeliveryRepository.getDriverAverageDeliveryTime(driverId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public DriverStatisticsDto getDriverStatistics(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        Double rating = getDriverRating(driverId, startDate, endDate);
        Double onTimePercentage = getDriverOnTimePercentage(driverId, startDate, endDate);
        Double avgDeliveryTime = getDriverAverageDeliveryTime(driverId, startDate, endDate);
        Long totalDeliveries = adminDeliveryRepository.getDriverTotalDeliveries(driverId, startDate, endDate);
        return new DriverStatisticsDto(
                rating,
                onTimePercentage,
                avgDeliveryTime,
                totalDeliveries
        );
    }

    /**
     * Get daily average ratings for a driver
     */
    @Transactional(readOnly = true)
    public List<DailyRatingDto> getDriverDailyRatings(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        List<DailyRatingDto> actualRatings = adminDeliveryRatingRepository
                .getDriverAverageRatingByDay(driverId, startDate, endDate);

        Map<LocalDate, Double> ratingMap = actualRatings.stream()
                .collect(Collectors.toMap(
                        DailyRatingDto::getDate,
                        DailyRatingDto::getAverageRating
                ));

        List<DailyRatingDto> completeRatings = new ArrayList<>();
        LocalDate currentDate = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        while (!currentDate.isAfter(end)) {
            Double rating = ratingMap.getOrDefault(currentDate, 0.0);
            completeRatings.add(new DailyRatingDto(currentDate, rating));
            currentDate = currentDate.plusDays(1);
        }

        return completeRatings;
    }

    /**
     * Get daily on-time percentage for a driver
     */
    @Transactional(readOnly = true)
    public List<DailyOnTimePercentageDto> getDriverDailyOnTimePercentage(Long driverId, LocalDateTime
            startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        List<DailyOnTimePercentageDto> actualPercentages = adminDeliveryRepository.getDriverOnTimePercentageByDay(driverId, startDate, endDate);

        Map<LocalDate, Double> onTimePercentageMap = actualPercentages.stream()
                .collect(Collectors.toMap(
                        DailyOnTimePercentageDto::getDate,
                        DailyOnTimePercentageDto::getOnTimePercentage
                ));

        List<DailyOnTimePercentageDto> completeOnTimePercentage = new ArrayList<>();
        LocalDate currentDate = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        while (!currentDate.isAfter(end)) {
            Double onTimePercentage = onTimePercentageMap.getOrDefault(currentDate, 0.0);
            completeOnTimePercentage.add(new DailyOnTimePercentageDto(currentDate, onTimePercentage));
            currentDate = currentDate.plusDays(1);
        }

        return completeOnTimePercentage;
    }

    /**
     * Get daily earning for a driver
     */
    @Transactional(readOnly = true)
    public List<DailyEarningDto> getDriverDailyEarning(Long driverId, LocalDateTime startDate, LocalDateTime
            endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        List<DailyEarningDto> actualPercentages = adminDriverDepositRepository.getDriverEarningByDay(driverId, startDate, endDate);

        Map<LocalDate, Double> onTimePercentageMap = actualPercentages.stream()
                .collect(Collectors.toMap(
                        DailyEarningDto::getDate,
                        DailyEarningDto::getEarning
                ));

        List<DailyEarningDto> completeOnTimePercentage = new ArrayList<>();
        LocalDate currentDate = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        while (!currentDate.isAfter(end)) {
            Double onTimePercentage = onTimePercentageMap.getOrDefault(currentDate, 0.0);
            completeOnTimePercentage.add(new DailyEarningDto(currentDate, onTimePercentage));
            currentDate = currentDate.plusDays(1);
        }

        return completeOnTimePercentage;
    }


    /**
     * Get ratings with comments for a driver
     */
    @Transactional(readOnly = true)
    public Page<DriverRatingCommentDto> getDriverRatingsWithComments(Long driverId, LocalDateTime startDate,
                                                                     LocalDateTime endDate, int page, int size) {
        validateDriverExists(driverId);
        Pageable pageable = PageRequest.of(page, size);
        return adminDeliveryRatingRepository.findRatingsWithCommentsByDriverId(
                driverId, startDate, endDate, pageable);
    }

    /**
     * Get rating distribution (1-5 stars) for a driver
     */
    @Transactional(readOnly = true)
    public List<RatingDistributionDto> getDriverRatingDistribution(Long driverId, LocalDateTime
            startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        return adminDeliveryRatingRepository.getDriverRatingDistribution(driverId, startDate, endDate);
    }

    /**
     * Get driver by ID
     */
    @Transactional(readOnly = true)
    public DriverDetailsDto getDriverById(Long driverId) {
        return adminDriverRepository.findDriverDetailsById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
    }

    /**
     * Get driver shifts with filters and pagination, including deliveries list for each shift
     */
    @Transactional(readOnly = true)
    public Page<AdminDriverShiftDto> getDriverShifts(Long driverId, LocalDateTime date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DriverShift> shiftsPage = adminShiftRepository.findShiftsWithFilters(driverId, date, pageable);
        return shiftsPage.map(this::convertToAdminDriverShiftDto);
    }

    public Page<DriverDepositAdminDto> getDepositsForAdmin(Long driverId, DriverDepositStatus status, int page,
                                                           int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<DriverDeposit> deposits = status != null
                ? adminDriverDepositRepository.findAllByDriverIdAndStatusOrderByCreatedAtDesc(driverId, status, pageable)
                : adminDriverDepositRepository.findAllByDriverIdOrderByCreatedAtDesc(driverId, pageable);
        return deposits.map(this::toAdminDto);
    }

    /**
     * Get driver earnings by payment method for a specific date
     */
    @Transactional(readOnly = true)
    public DriverEarningsByPaymentMethodDto getDriverEarningsByPaymentMethod(Long driverId, LocalDate date) {
        validateDriverExists(driverId);

        Double cashEarnings = adminDeliveryRepository.getEarningByDriverIdAndDeliveredAtDateAndPaymentMethod(driverId, date, "CASH");
        Double cardEarnings = adminDeliveryRepository.getEarningByDriverIdAndDeliveredAtDateAndPaymentMethod(driverId, date, "CARD");

        // Handle null values and calculate total
        Double cash = cashEarnings != null ? cashEarnings : 0.0;
        Double card = cardEarnings != null ? cardEarnings : 0.0;
        Double total = cash + card;
        Double totalCommission = total * DEFAULT_DRIVER_COMMISSION_RATE;

        return DriverEarningsByPaymentMethodDto.builder()
                .totalCommission(totalCommission)
                .cashEarnings(cash)
                .cardEarnings(card)
                .totalEarnings(total)
                .build();
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

        DriverDepositCalculation calculation = calculateDepositForDriver(driver);
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

        DriverDepositCalculation calculation = calculateDepositForDriver(driver);
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

    private boolean isDriverPaid(Driver driver) {
        BigDecimal zero = BigDecimal.ZERO;

        boolean noCashOnHand = driver.getCashOnHand() == null ||
                driver.getCashOnHand().compareTo(zero) <= 0;

        boolean noUnpaidEarnings = driver.getUnpaidEarnings() == null ||
                driver.getUnpaidEarnings().compareTo(zero) <= 0;

        boolean noOutstandingFees = driver.getOutstandingDailyFees() == null ||
                driver.getOutstandingDailyFees().compareTo(zero) <= 0;

        return noCashOnHand && noUnpaidEarnings && noOutstandingFees;
    }

    private AdminDriverShiftDto convertToAdminDriverShiftDto(DriverShift shift) {
        Driver driver = shift.getDriver();
        DriverShiftBalance balance = shift.getBalance();

        List<AdminShiftDeliveryDto> deliveryDtos = shift.getDeliveries().stream()
                .map(this::convertToAdminShiftDeliveryDto)
                .toList();

        BigDecimal zeroAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        return AdminDriverShiftDto.builder()
                .id(shift.getId())
                .driverId(driver != null ? driver.getId() : null)
                .driverName(driver != null ? driver.getName() : null)
                .status(shift.getStatus())
                .startedAt(shift.getStartedAt())
                .finishableAt(shift.getFinishableAt())
                .endedAt(shift.getEndedAt())
                .totalAmount(balance != null ? balance.getTotalAmount() : zeroAmount)
                .driverShare(balance != null ? balance.getDriverShare() : zeroAmount)
                .restaurantShare(balance != null ? balance.getRestaurantShare() : zeroAmount)
                .settled(balance != null && balance.isSettled())
                .settledAt(balance != null ? balance.getSettledAt() : null)
                .deliveries(deliveryDtos)
                .build();
    }

    private AdminShiftDeliveryDto convertToAdminShiftDeliveryDto(Delivery delivery) {
        Order order = delivery.getOrder();
        DeliveryRating rating = delivery.getRating();

        String deliveryAddress = null;
        if (order != null && order.getSavedAddress() != null) {
            deliveryAddress = order.getSavedAddress().getFormattedAddress();
        }

        String restaurantName = null;
        if (order != null && order.getRestaurant() != null) {
            restaurantName = order.getRestaurant().getName();
        }

        return AdminShiftDeliveryDto.builder()
                .id(delivery.getId())
                .orderId(order != null ? order.getId() : null)
                .restaurantName(restaurantName)
                .deliveryAddress(deliveryAddress)
                .deliveryTime(delivery.getDeliveryTime())
                .timeToPickUp(delivery.getTimeToPickUp())
                .assignedTime(delivery.getAssignedTime())
                .pickupTime(delivery.getPickupTime())
                .deliveredTime(delivery.getDeliveredTime())
                .rating(rating != null ? rating.getOverallRating().doubleValue() : null)
                .ratingComment(rating != null ? rating.getComments() : null)
                .build();
    }

    private void validateDriverExists(Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        if (!adminDriverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Start date must be before or equal to end date");
        }
    }

    private DriverDepositAdminDto depositToAdminDto(DriverDeposit deposit) {
        if (deposit == null) {
            return null;
        }
        Driver driver = deposit.getDriver();
        return DriverDepositAdminDto.builder()
                .id(deposit.getId())
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
        driver.setOutstandingDailyFees(calculateDailyFeeAmount(updatedDays));
        driver.setLastDailyFeeDate(today);
        adminDriverRepository.save(driver);
    }

    private BigDecimal calculateDailyFeeAmount(int days) {
        if (days <= 0) {
            return ZERO;
        }
        return DAILY_FEE.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }

    private static class DriverDepositCalculation {
        private final BigDecimal cashOnHand;
        private final BigDecimal unpaidEarnings;
        private final int outstandingDays;
        private final BigDecimal outstandingFees;
        private final BigDecimal feesDeducted;
        private final BigDecimal netPayout;

        private DriverDepositCalculation(BigDecimal cashOnHand, BigDecimal unpaidEarnings, int outstandingDays,
                                         BigDecimal outstandingFees, BigDecimal feesDeducted, BigDecimal netPayout) {
            this.cashOnHand = cashOnHand;
            this.unpaidEarnings = unpaidEarnings;
            this.outstandingDays = outstandingDays;
            this.outstandingFees = outstandingFees;
            this.feesDeducted = feesDeducted;
            this.netPayout = netPayout;
        }

        public BigDecimal getCashOnHand() {
            return cashOnHand;
        }

        public BigDecimal getUnpaidEarnings() {
            return unpaidEarnings;
        }

        public int getOutstandingDays() {
            return outstandingDays;
        }

        public BigDecimal getOutstandingFees() {
            return outstandingFees;
        }

        public BigDecimal getFeesDeducted() {
            return feesDeducted;
        }

        public BigDecimal getNetPayout() {
            return netPayout;
        }
    }

    private DriverDepositCalculation calculateDepositForDriver(Driver driver) {
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
        BigDecimal feesDeducted = calculateDailyFeesToDeduct(unpaidEarnings, outstandingDays)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal netPayout = unpaidEarnings.subtract(feesDeducted);
        if (netPayout.compareTo(BigDecimal.ZERO) < 0) {
            netPayout = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            netPayout = netPayout.setScale(2, RoundingMode.HALF_UP);
        }

        return new DriverDepositCalculation(cashOnHand, unpaidEarnings, outstandingDays, outstandingFees, feesDeducted, netPayout);
    }

    private BigDecimal calculateDailyFeesToDeduct(BigDecimal unpaidEarnings, int outstandingDays) {
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

    private DriverDeposit createPendingDepositForDriver(Driver driver, DriverDepositCalculation calculation) {
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

        driver.setCashOnHand(ZERO);
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
            BigDecimal updatedEarnings = normalize(driver.getUnpaidEarnings())
                    .subtract(normalize(deposit.getEarningsPaid()));
            if (updatedEarnings.compareTo(BigDecimal.ZERO) < 0) {
                updatedEarnings = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
            driver.setUnpaidEarnings(updatedEarnings);

            BigDecimal deductedFees = normalize(deposit.getFeesDeducted());
            int paidDays = deductedFees.divide(DAILY_FEE, 0, RoundingMode.DOWN).intValue();
            int currentOutstandingDays = Math.max(0, Optional.ofNullable(driver.getOutstandingDailyFeeDays()).orElse(0));
            int updatedOutstandingDays = Math.max(0, currentOutstandingDays - paidDays);
            driver.setOutstandingDailyFeeDays(updatedOutstandingDays);
            driver.setOutstandingDailyFees(calculateDailyFeeAmount(updatedOutstandingDays));

            // Clear the deposit warning timestamp since deposit is confirmed
            driver.setDepositWarningSentAt(null);

            adminDriverRepository.save(driver);
            Long driverId = driver.getId();
            if (driverId != null) {
                eventPublisher.publishEvent(new DriverDepositConfirmedEvent(driverId));
            }
        }

        adminDriverDepositRepository.save(deposit);
        return depositToAdminDto(deposit);
    }

    private DriverDepositAdminDto toAdminDto(DriverDeposit deposit) {
        if (deposit == null) {
            return null;
        }
        Driver driver = deposit.getDriver();
        return DriverDepositAdminDto.builder()
                .id(deposit.getId())
                .depositAmount(normalize(deposit.getDepositAmount()))
                .earningsPaid(normalize(deposit.getEarningsPaid()))
                .feesDeducted(normalize(deposit.getFeesDeducted()))
                .status(deposit.getStatus())
                .createdAt(deposit.getCreatedAt())
                .confirmedAt(deposit.getConfirmedAt())
                .build();
    }

}
