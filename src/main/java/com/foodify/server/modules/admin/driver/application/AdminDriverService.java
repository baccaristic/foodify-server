package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.*;
import com.foodify.server.modules.admin.driver.repository.*;
import com.foodify.server.modules.delivery.domain.*;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDriverService {

    private final AdminDriverRepository adminDriverRepository;
    private final AdminDeliveryRepository adminDeliveryRepository;
    private final AdminDeliveryRatingRepository adminDeliveryRatingRepository;
    private final AdminDriverDepositRepository adminDriverDepositRepository;
    private final AdminShiftRepository adminShiftRepository;

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    /**
     * Get paginated list of drivers with filters
     */
    @Transactional(readOnly = true)
    public Page<DriverListItemDto> getDriverList(String query, DriverDepositStatus depositStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driversPage = adminDriverRepository.findAll(
                DriverSpecifications.withFilters(query, depositStatus),
                pageable
        );

        return driversPage.map(driver -> new DriverListItemDto(
                driver.getId(),
                driver.getName(),
                driver.getPhone(),
                depositStatus // This should ideally be fetched from the actual deposit status
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
    public List<DailyOnTimePercentageDto> getDriverDailyOnTimePercentage(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
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
    public List<DailyEarningDto> getDriverDailyEarning(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
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
    public List<RatingDistributionDto> getDriverRatingDistribution(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
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

    public Page<DriverDepositAdminDto> getDepositsForAdmin(Long driverId, DriverDepositStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<DriverDeposit> deposits = status != null
                ? adminDriverDepositRepository.findAllByDriverIdAndStatusOrderByCreatedAtDesc(driverId, status,pageable)
                : adminDriverDepositRepository.findAllByDriverIdOrderByCreatedAtDesc(driverId,pageable);
        return deposits.map(this::toAdminDto);
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

    private BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }


}
