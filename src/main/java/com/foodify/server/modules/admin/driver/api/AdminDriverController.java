package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.AdminDriverService;
import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import com.foodify.server.modules.admin.driver.dto.DriverRatingCommentDto;
import com.foodify.server.modules.admin.driver.dto.DriverStatisticsDto;
import com.foodify.server.modules.admin.driver.dto.RatingDistributionDto;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverController {

    private final AdminDriverService adminDriverService;

    /**
     * Get paginated list of drivers with optional filters
     * GET /api/admin/drivers?query=search&depositStatus=CONFIRMED&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<DriverListItemDto>> getDrivers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) DriverDepositStatus depositStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<DriverListItemDto> drivers = adminDriverService.getDriverList(query, depositStatus, page, size);
        return ResponseEntity.ok(drivers);
    }

    /**
     * Get driver by ID
     * GET /api/admin/drivers/{driverId}
     */
    @GetMapping("/{driverId}")
    public ResponseEntity<Driver> getDriver(@PathVariable Long driverId) {
        Driver driver = adminDriverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }

    /**
     * Get driver statistics (rating, on-time percentage, avg delivery time)
     * GET /api/admin/drivers/{driverId}/statistics?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/statistics")
    public ResponseEntity<DriverStatisticsDto> getDriverStatistics(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Double rating = adminDriverService.getDriverRating(driverId, startDate, endDate);
        Double onTimePercentage = adminDriverService.getDriverOnTimePercentage(driverId, startDate, endDate);
        Double avgDeliveryTime = adminDriverService.getDriverAverageDeliveryTime(driverId, startDate, endDate);
        
        DriverStatisticsDto statistics = new DriverStatisticsDto(
                driverId,
                rating,
                onTimePercentage,
                avgDeliveryTime
        );
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get driver rating for a period
     * GET /api/admin/drivers/{driverId}/rating?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/rating")
    public ResponseEntity<Double> getDriverRating(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double rating = adminDriverService.getDriverRating(driverId, startDate, endDate);
        return ResponseEntity.ok(rating);
    }

    /**
     * Get driver on-time percentage
     * GET /api/admin/drivers/{driverId}/on-time-percentage?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/on-time-percentage")
    public ResponseEntity<Double> getDriverOnTimePercentage(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double percentage = adminDriverService.getDriverOnTimePercentage(driverId, startDate, endDate);
        return ResponseEntity.ok(percentage);
    }

    /**
     * Get driver average delivery time
     * GET /api/admin/drivers/{driverId}/average-delivery-time?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/average-delivery-time")
    public ResponseEntity<Double> getDriverAverageDeliveryTime(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double avgTime = adminDriverService.getDriverAverageDeliveryTime(driverId, startDate, endDate);
        return ResponseEntity.ok(avgTime);
    }

    /**
     * Get daily ratings trend for a driver
     * GET /api/admin/drivers/{driverId}/daily-ratings?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/daily-ratings")
    public ResponseEntity<List<DailyRatingDto>> getDailyRatings(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyRatingDto> dailyRatings = adminDriverService.getDriverDailyRatings(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyRatings);
    }

    /**
     * Get daily on-time percentage trend for a driver
     * GET /api/admin/drivers/{driverId}/daily-on-time?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/daily-on-time")
    public ResponseEntity<List<DailyOnTimePercentageDto>> getDailyOnTimePercentage(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyOnTimePercentageDto> dailyData = adminDriverService.getDriverDailyOnTimePercentage(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyData);
    }

    /**
     * Get ratings with comments for a driver
     * GET /api/admin/drivers/{driverId}/ratings-with-comments?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=20
     */
    @GetMapping("/{driverId}/ratings-with-comments")
    public ResponseEntity<Page<DriverRatingCommentDto>> getRatingsWithComments(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<DeliveryRating> ratingsPage = adminDriverService.getDriverRatingsWithComments(
                driverId, startDate, endDate, page, size);
        
        Page<DriverRatingCommentDto> dtoPage = ratingsPage.map(rating -> {
            String clientName = rating.getDelivery() != null 
                    && rating.getDelivery().getOrder() != null 
                    && rating.getDelivery().getOrder().getClient() != null
                    ? rating.getDelivery().getOrder().getClient().getName()
                    : null;
            
            Long orderId = rating.getDelivery() != null 
                    && rating.getDelivery().getOrder() != null
                    ? rating.getDelivery().getOrder().getId()
                    : null;
            
            return new DriverRatingCommentDto(
                    rating.getDeliveryId(),
                    rating.getDelivery() != null ? rating.getDelivery().getId() : null,
                    orderId,
                    clientName,
                    rating.getTimingRating(),
                    rating.getFoodConditionRating(),
                    rating.getProfessionalismRating(),
                    rating.getOverallRating(),
                    rating.getComments(),
                    rating.getCreatedAt()
            );
        });
        
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get rating distribution (1-5 stars count) for a driver
     * GET /api/admin/drivers/{driverId}/rating-distribution?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/rating-distribution")
    public ResponseEntity<RatingDistributionDto> getRatingDistribution(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Long> distribution = adminDriverService.getDriverRatingDistribution(driverId, startDate, endDate);
        RatingDistributionDto dto = new RatingDistributionDto(driverId, distribution);
        
        return ResponseEntity.ok(dto);
    }
}
