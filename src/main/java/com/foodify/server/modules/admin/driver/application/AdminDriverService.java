package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRatingRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverRepository;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDriverService {

    private final AdminDriverRepository adminDriverRepository;
    private final AdminDeliveryRepository adminDeliveryRepository;
    private final AdminDeliveryRatingRepository adminDeliveryRatingRepository;

    /**
     * Get paginated list of drivers with filters
     */
    @Transactional(readOnly = true)
    public Page<DriverListItemDto> getDriverList(String query, DriverDepositStatus depositStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driversPage = adminDriverRepository.findWithFilters(query, depositStatus, pageable);
        
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
        return adminDeliveryRepository.getDriverAverageRatingByDay(driverId, startDate, endDate);
    }

    /**
     * Get daily on-time percentage for a driver
     */
    @Transactional(readOnly = true)
    public List<DailyOnTimePercentageDto> getDriverDailyOnTimePercentage(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        validateDateRange(startDate, endDate);
        return adminDeliveryRepository.getDriverOnTimePercentageByDay(driverId, startDate, endDate);
    }

    /**
     * Get ratings with comments for a driver
     */
    @Transactional(readOnly = true)
    public Page<DeliveryRating> getDriverRatingsWithComments(Long driverId, LocalDateTime startDate, 
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
    public List<Long> getDriverRatingDistribution(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        return adminDeliveryRatingRepository.getDriverRatingDistribution(driverId, startDate, endDate);
    }

    /**
     * Get driver by ID
     */
    @Transactional(readOnly = true)
    public Driver getDriverById(Long driverId) {
        return adminDriverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
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
}
