package com.foodify.server.modules.admin.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import com.foodify.server.modules.admin.dto.*;
import com.foodify.server.modules.delivery.dto.DriverRatingSummaryDto;
import com.foodify.server.modules.delivery.dto.DeliveryRatingResponse;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRatingRepository;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.delivery.repository.DriverDepositRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverManagementService {
    
    // Configuration constants
    private static final BigDecimal DAILY_FEE = BigDecimal.valueOf(5.0);
    private static final BigDecimal CASH_PAYMENT_RATIO = BigDecimal.valueOf(0.3);
    private static final BigDecimal CARD_PAYMENT_RATIO = BigDecimal.valueOf(0.7);
    private static final BigDecimal COMMISSION_RATE = BigDecimal.valueOf(0.15);

    private final DriverRepository driverRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRatingRepository deliveryRatingRepository;
    private final OrderRepository orderRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final DriverDepositRepository driverDepositRepository;
    private final DriverLocationService driverLocationService;

    @Transactional(readOnly = true)
    public TodayRevenueDto getTodayRevenue() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

        // Get all shifts for today and yesterday
        List<DriverShift> allShifts = driverShiftRepository.findAll();
        
        BigDecimal todayRevenue = allShifts.stream()
                .filter(shift -> shift.getStartedAt() != null 
                        && shift.getStartedAt().isAfter(todayStart) 
                        && shift.getStartedAt().isBefore(todayEnd))
                .map(DriverShift::getBalance)
                .filter(Objects::nonNull)
                .map(DriverShiftBalance::getDriverShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal yesterdayRevenue = allShifts.stream()
                .filter(shift -> shift.getStartedAt() != null 
                        && shift.getStartedAt().isAfter(yesterdayStart) 
                        && shift.getStartedAt().isBefore(yesterdayEnd))
                .map(DriverShift::getBalance)
                .filter(Objects::nonNull)
                .map(DriverShiftBalance::getDriverShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double percentageChange = 0.0;
        if (yesterdayRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = todayRevenue.subtract(yesterdayRevenue);
            percentageChange = difference.divide(yesterdayRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        return TodayRevenueDto.builder()
                .todayRevenue(todayRevenue)
                .yesterdayRevenue(yesterdayRevenue)
                .percentageChange(percentageChange)
                .build();
    }

    @Transactional(readOnly = true)
    public ActiveDriversDto getActiveDrivers() {
        long activeCount = driverRepository.countByAvailableTrue();
        long totalCount = driverRepository.count();

        return ActiveDriversDto.builder()
                .activeDriversCount(activeCount)
                .totalDriversCount(totalCount)
                .build();
    }

    @Transactional(readOnly = true)
    public DailyMembershipCollectionDto getDailyMembershipCollection() {
        LocalDate today = LocalDate.now();
        List<Driver> allDrivers = driverRepository.findAll();

        BigDecimal totalCollected = BigDecimal.ZERO;
        long driversWithPayment = 0;

        for (Driver driver : allDrivers) {
            if (driver.getLastDailyFeeDate() != null && driver.getLastDailyFeeDate().equals(today)) {
                totalCollected = totalCollected.add(DAILY_FEE);
                driversWithPayment++;
            }
        }

        BigDecimal expectedTotal = DAILY_FEE.multiply(BigDecimal.valueOf(allDrivers.size()));
        double percentage = expectedTotal.compareTo(BigDecimal.ZERO) > 0
                ? totalCollected.divide(expectedTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

        return DailyMembershipCollectionDto.builder()
                .totalCollected(totalCollected)
                .expectedTotal(expectedTotal)
                .collectionPercentage(percentage)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<AdminDriverListItemDto> getDriversWithFilters(
            String query,
            Boolean paymentStatus,
            Boolean isOnline,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> drivers = driverRepository.findDriversWithFilters(query, paymentStatus, isOnline, pageable);

        return drivers.map(driver -> {
            DriverRatingSummaryDto ratingSummary = deliveryRatingRepository.findSummaryByDriverId(driver.getId())
                    .orElse(DriverRatingSummaryDto.empty(driver.getId()));
            long totalOrders = deliveryRepository.countCompletedByDriverId(driver.getId());
            boolean paymentUpToDate = driver.getOutstandingDailyFees().compareTo(BigDecimal.ZERO) == 0;

            return AdminDriverListItemDto.builder()
                    .id(driver.getId())
                    .name(driver.getName())
                    .email(driver.getEmail())
                    .phone(driver.getPhone())
                    .isOnline(driver.isAvailable())
                    .paymentStatus(paymentUpToDate)
                    .averageRating(ratingSummary.overallAverage())
                    .totalOrders(totalOrders)
                    .unpaidEarnings(driver.getUnpaidEarnings())
                    .outstandingDailyFees(driver.getOutstandingDailyFees())
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public DriverRatingSummaryDto getDriverRating(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
        return deliveryRatingRepository.findSummaryByDriverId(driverId)
                .orElse(DriverRatingSummaryDto.empty(driverId));
    }

    @Transactional(readOnly = true)
    public Long getTotalOrders(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
        return deliveryRepository.countCompletedByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public Long getOnTimeOrders(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
        return deliveryRepository.countOnTimeByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public Long getCanceledOrders(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
        return orderRepository.countByDriverIdAndStatus(driverId, OrderStatus.CANCELED);
    }

    @Transactional(readOnly = true)
    public DriverDetailsDto getDriverDetails(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        DriverRatingSummaryDto ratingSummary = deliveryRatingRepository.findSummaryByDriverId(driverId)
                .orElse(DriverRatingSummaryDto.empty(driverId));
        long totalOrders = deliveryRepository.countCompletedByDriverId(driverId);
        long onTimeOrders = deliveryRepository.countOnTimeByDriverId(driverId);
        long canceledOrders = orderRepository.countByDriverIdAndStatus(driverId, OrderStatus.CANCELED);

        return DriverDetailsDto.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .isOnline(driver.isAvailable())
                .averageRating(ratingSummary.overallAverage())
                .totalOrders(totalOrders)
                .onTimeOrders(onTimeOrders)
                .canceledOrders(canceledOrders)
                .unpaidEarnings(driver.getUnpaidEarnings())
                .outstandingDailyFees(driver.getOutstandingDailyFees())
                .lastDailyFeeDate(driver.getLastDailyFeeDate())
                .joinedAt(null) // Note: joinedAt field not available in current Driver entity
                .build();
    }

    @Transactional(readOnly = true)
    public Boolean getCurrentStatus(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
        return driver.isAvailable();
    }

    @Transactional(readOnly = true)
    public Optional<DriverCurrentTaskDto> getCurrentTask(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        Optional<Order> currentOrder = orderRepository.findByDriverIdAndStatusIn(
                driverId,
                Arrays.asList(OrderStatus.IN_DELIVERY, OrderStatus.READY_FOR_PICK_UP)
        );

        return currentOrder.map(order -> DriverCurrentTaskDto.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .restaurantName(order.getRestaurant() != null ? order.getRestaurant().getName() : null)
                .deliveryAddress(order.getDeliveryAddress())
                .estimatedDeliveryTime(order.getEstimatedReadyAt())
                .restaurantLatitude(order.getRestaurant() != null ? order.getRestaurant().getLatitude() : null)
                .restaurantLongitude(order.getRestaurant() != null ? order.getRestaurant().getLongitude() : null)
                .deliveryLatitude(order.getLat())
                .deliveryLongitude(order.getLng())
                .build());
    }

    @Transactional(readOnly = true)
    public Optional<DriverLocationResponseDto> getCurrentLocation(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        Point location = driverLocationService.getLastKnownPosition(driverId);
        if (location == null) {
            return Optional.empty();
        }

        return Optional.of(DriverLocationResponseDto.builder()
                .driverId(driverId)
                .latitude(location.getY())
                .longitude(location.getX())
                .lastUpdated(LocalDateTime.now())
                .build());
    }

    @Transactional(readOnly = true)
    public DeliveryMetricsDto getDeliveryMetrics(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        long totalCompleted = deliveryRepository.countCompletedByDriverId(driverId);
        long totalOnTime = deliveryRepository.countOnTimeByDriverId(driverId);
        Double avgDuration = deliveryRepository.getAverageDeliveryTimeByDriverId(driverId);

        // Note: Actual implementation would require tracking of order offers to drivers
        // For now, using completed orders as a baseline
        // Future enhancement: Track order assignments separately
        long totalOffered = totalCompleted; // Simplified assumption
        long totalAccepted = totalCompleted; // Simplified assumption

        double acceptanceRate = totalOffered > 0 ? (totalAccepted * 100.0 / totalOffered) : 0.0;
        double completionRate = totalAccepted > 0 ? (totalCompleted * 100.0 / totalAccepted) : 0.0;
        double onTimeRate = totalCompleted > 0 ? (totalOnTime * 100.0 / totalCompleted) : 0.0;

        return DeliveryMetricsDto.builder()
                .acceptanceRate(acceptanceRate)
                .completionRate(completionRate)
                .onTimeRate(onTimeRate)
                .avgDeliveryDuration(avgDuration != null ? avgDuration : 0.0)
                .totalOffered(totalOffered)
                .totalAccepted(totalAccepted)
                .totalCompleted(totalCompleted)
                .totalOnTime(totalOnTime)
                .build();
    }

    @Transactional(readOnly = true)
    public DriverMonthlyStatsDto getMonthlyStats(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        List<Object[]> dailyRatings = deliveryRatingRepository.findDailyAverageRatingsByDriverIdAndDateRange(
                driverId, startDate, endDate);
        List<Delivery> deliveries = deliveryRepository.findByDriverIdAndDateRange(driverId, startDate, endDate);

        Map<LocalDate, Double> ratingsByDate = new HashMap<>();
        for (Object[] row : dailyRatings) {
            LocalDate date = (LocalDate) row[0];
            Double avgRating = (Double) row[1];
            ratingsByDate.put(date, avgRating);
        }

        Map<LocalDate, List<Delivery>> deliveriesByDate = deliveries.stream()
                .collect(Collectors.groupingBy(d -> d.getDeliveredTime().toLocalDate()));

        List<DriverMonthlyStatsDto.DailyStatDto> dailyStats = new ArrayList<>();
        LocalDate currentDate = startDate.toLocalDate();
        while (!currentDate.isAfter(endDate.toLocalDate())) {
            Double avgRating = ratingsByDate.getOrDefault(currentDate, 0.0);
            List<Delivery> dayDeliveries = deliveriesByDate.getOrDefault(currentDate, Collections.emptyList());
            
            long onTimeCount = dayDeliveries.stream()
                    .filter(d -> d.getDeliveryTime() != null && d.getDeliveryTime() <= 30)
                    .count();
            double onTimePercentage = dayDeliveries.isEmpty() ? 0.0 : (onTimeCount * 100.0 / dayDeliveries.size());

            dailyStats.add(DriverMonthlyStatsDto.DailyStatDto.builder()
                    .date(currentDate)
                    .averageRating(avgRating)
                    .onTimePercentage(onTimePercentage)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return DriverMonthlyStatsDto.builder()
                .dailyStats(dailyStats)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<DeliveryRatingResponse> getCustomerRatings(Long driverId, int page, int size) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<DeliveryRating> ratings = deliveryRatingRepository.findByDriverIdWithDetails(driverId, pageable);

        return ratings.map(rating -> new DeliveryRatingResponse(
                rating.getDelivery().getOrder().getId(),
                rating.getDeliveryId(),
                driverId,
                rating.getDelivery().getOrder().getClient() != null 
                        ? rating.getDelivery().getOrder().getClient().getId() 
                        : null,
                rating.getDelivery().getOrder().getClient() != null 
                        ? rating.getDelivery().getOrder().getClient().getName() 
                        : "Unknown",
                rating.getTimingRating(),
                rating.getFoodConditionRating(),
                rating.getProfessionalismRating(),
                rating.getOverallRating(),
                rating.getComments(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        ));
    }

    @Transactional(readOnly = true)
    public RatingDistributionDto getRatingDistribution(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        long oneStarCount = deliveryRatingRepository.countByDriverIdAndRating(driverId, 1);
        long twoStarCount = deliveryRatingRepository.countByDriverIdAndRating(driverId, 2);
        long threeStarCount = deliveryRatingRepository.countByDriverIdAndRating(driverId, 3);
        long fourStarCount = deliveryRatingRepository.countByDriverIdAndRating(driverId, 4);
        long fiveStarCount = deliveryRatingRepository.countByDriverIdAndRating(driverId, 5);

        return RatingDistributionDto.builder()
                .oneStarCount(oneStarCount)
                .twoStarCount(twoStarCount)
                .threeStarCount(threeStarCount)
                .fourStarCount(fourStarCount)
                .fiveStarCount(fiveStarCount)
                .totalRatings(oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ShiftHistoryDto> getShiftHistory(Long driverId, LocalDate date, int page, int size) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        LocalDateTime dateTime = date.atStartOfDay();
        Pageable pageable = PageRequest.of(page, size);
        Page<DriverShift> shifts = driverShiftRepository.findByDriverIdAndDate(driverId, dateTime, pageable);

        return shifts.map(shift -> {
            List<ShiftHistoryDto.ShiftOrderOverviewDto> orders = shift.getDeliveries().stream()
                    .map(delivery -> ShiftHistoryDto.ShiftOrderOverviewDto.builder()
                            .orderId(delivery.getOrder().getId())
                            .restaurantName(delivery.getOrder().getRestaurant() != null 
                                    ? delivery.getOrder().getRestaurant().getName() 
                                    : "Unknown")
                            .status(delivery.getOrder().getStatus().toString())
                            .earnings(delivery.getOrder().getDeliveryFee())
                            .deliveryTime(delivery.getDeliveredTime())
                            .build())
                    .collect(Collectors.toList());

            BigDecimal totalEarnings = shift.getBalance() != null 
                    ? shift.getBalance().getDriverShare() 
                    : BigDecimal.ZERO;

            return ShiftHistoryDto.builder()
                    .shiftId(shift.getId())
                    .startTime(shift.getStartedAt())
                    .endTime(shift.getEndedAt())
                    .totalEarnings(totalEarnings)
                    .totalOrders(orders.size())
                    .orders(orders)
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public TodayEarningsDto getTodayEarnings(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<DriverShift> todayShifts = driverShiftRepository.findAllWithBalanceByDriverIdAndStartedAtBetweenOrderByStartedAtDesc(
                driverId, todayStart, todayEnd);

        BigDecimal totalEarnings = BigDecimal.ZERO;
        BigDecimal cashEarnings = BigDecimal.ZERO;
        BigDecimal cardEarnings = BigDecimal.ZERO;
        int deliveryCount = 0;

        for (DriverShift shift : todayShifts) {
            if (shift.getBalance() != null) {
                totalEarnings = totalEarnings.add(shift.getBalance().getDriverShare());
                // Note: Payment method tracking per order not currently available in the model
                // Using estimated ratios for cash/card breakdown
                deliveryCount += shift.getDeliveries().size();
            }
        }

        // Estimated breakdown - actual implementation would need payment method per order
        cashEarnings = totalEarnings.multiply(CASH_PAYMENT_RATIO);
        cardEarnings = totalEarnings.multiply(CARD_PAYMENT_RATIO);
        BigDecimal commission = totalEarnings.multiply(COMMISSION_RATE);

        return TodayEarningsDto.builder()
                .totalEarnings(totalEarnings)
                .cashEarnings(cashEarnings)
                .cardEarnings(cardEarnings)
                .commission(commission)
                .numberOfDeliveries(deliveryCount)
                .build();
    }

    @Transactional(readOnly = true)
    public DailySubscriptionDueDateDto getDailySubscriptionDueDate(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        LocalDate lastPayment = driver.getLastDailyFeeDate();
        LocalDate nextDue = lastPayment != null ? lastPayment.plusDays(1) : LocalDate.now();
        int daysPastDue = driver.getOutstandingDailyFeeDays();

        return DailySubscriptionDueDateDto.builder()
                .lastPaymentDate(lastPayment)
                .nextDueDate(nextDue)
                .daysPastDue(daysPastDue)
                .amountDue(driver.getOutstandingDailyFees())
                .build();
    }

    @Transactional
    public DriverDepositAdminDto createPayment(Long driverId, CreatePaymentRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));

        DriverDeposit deposit = new DriverDeposit();
        deposit.setDriver(driver);
        deposit.setDepositAmount(request.getAmount());
        deposit.setEarningsPaid(BigDecimal.ZERO);
        deposit.setFeesDeducted(BigDecimal.ZERO);

        DriverDeposit savedDeposit = driverDepositRepository.save(deposit);

        return DriverDepositAdminDto.builder()
                .id(savedDeposit.getId())
                .driverId(driver.getId())
                .driverName(driver.getName())
                .driverPhone(driver.getPhone())
                .depositAmount(savedDeposit.getDepositAmount())
                .earningsPaid(savedDeposit.getEarningsPaid())
                .feesDeducted(savedDeposit.getFeesDeducted())
                .status(savedDeposit.getStatus())
                .createdAt(savedDeposit.getCreatedAt())
                .confirmedAt(savedDeposit.getConfirmedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<PaymentHistoryDto> getPaymentHistory(Long driverId, int page, int size) {
        if (!driverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }

        Pageable pageable = PageRequest.of(page, size);
        List<DriverDeposit> deposits = driverDepositRepository.findByDriver_IdOrderByCreatedAtDesc(driverId);
        
        // Convert to Page manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), deposits.size());
        
        List<PaymentHistoryDto> content = deposits.subList(start, end).stream()
                .map(deposit -> PaymentHistoryDto.builder()
                        .id(deposit.getId())
                        .amount(deposit.getDepositAmount())
                        .paymentMethod("CASH") // Could be extended
                        .notes("Deposit")
                        .createdAt(deposit.getCreatedAt())
                        .confirmedByAdminName(deposit.getConfirmedBy() != null 
                                ? deposit.getConfirmedBy().getName() 
                                : null)
                        .build())
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(content, pageable, deposits.size());
    }
}
