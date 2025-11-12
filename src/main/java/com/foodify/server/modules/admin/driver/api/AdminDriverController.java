package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.AdminDriverService;
import com.foodify.server.modules.admin.driver.dto.*;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.admin.driver.dto.DriverDepositAdminDto;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverDetailsDto> getDriver(@PathVariable Long driverId) {
        DriverDetailsDto driver = adminDriverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }

    /**
     * Get driver shifts with filters and pagination, including deliveries for each shift
     * GET /api/admin/drivers/{driverId}/shifts?date=2024-01-01T00:00:00&page=0&size=20
     */
    @GetMapping("/{driverId}/shifts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminDriverShiftDto>> getDriverShifts(
            @PathVariable Long driverId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AdminDriverShiftDto> shifts = adminDriverService.getDriverShifts(driverId, date, page, size);
        return ResponseEntity.ok(shifts);
    }

    /**
     * Get driver statistics (rating, on-time percentage, avg delivery time)
     * GET /api/admin/drivers/{driverId}/statistics?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

     @GetMapping("/{driverId}/on-time-percentage")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')") public ResponseEntity<Double> getDriverOnTimePercentage(
     @PathVariable Long driverId,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
     Double percentage = adminDriverService.getDriverOnTimePercentage(driverId, startDate, endDate);
     return ResponseEntity.ok(percentage);
     }
     */
    /**
     * Get driver average delivery time
     * GET /api/admin/drivers/{driverId}/average-delivery-time?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     @GetMapping("/{driverId}/average-delivery-time")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')") public ResponseEntity<Double> getDriverAverageDeliveryTime(
     @PathVariable Long driverId,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
     Double avgTime = adminDriverService.getDriverAverageDeliveryTime(driverId, startDate, endDate);
     return ResponseEntity.ok(avgTime);
     }
     */

    /**
     * Get daily ratings trend for a driver
     * GET /api/admin/drivers/{driverId}/daily-ratings?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/daily-ratings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyOnTimePercentageDto>> getDailyOnTimePercentage(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyOnTimePercentageDto> dailyData = adminDriverService.getDriverDailyOnTimePercentage(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyData);
    }

    /**
     * Get daily earning trend for a driver
     * GET /api/admin/drivers/{driverId}/daily-earning?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/daily-earning")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyEarningDto>> getDailyEarning(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyEarningDto> dailyData = adminDriverService.getDriverDailyEarning(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyData);
    }

    /**
     * Get ratings with comments for a driver
     * GET /api/admin/drivers/{driverId}/ratings-with-comments?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=20
     */
    @GetMapping("/{driverId}/ratings-with-comments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<DriverRatingCommentDto>> getRatingsWithComments(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<DriverRatingCommentDto> dtoPage = adminDriverService.getDriverRatingsWithComments(
                driverId, startDate, endDate, page, size);

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get rating distribution (1-5 stars count) for a driver
     * GET /api/admin/drivers/{driverId}/rating-distribution?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/{driverId}/rating-distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<RatingDistributionDto>> getRatingDistribution(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<RatingDistributionDto> distribution = adminDriverService.getDriverRatingDistribution(driverId, startDate, endDate);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/{driverId}/deposits")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<DriverDepositAdminDto> listDeposits(
            @PathVariable Long driverId,
            @RequestParam(value = "status", required = false) DriverDepositStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return adminDriverService.getDepositsForAdmin(driverId, status, page, size);
    }


/*
    @GetMapping("/{driverId}/earnings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverEarningsSummaryDto getEarningsSummary(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {

        return adminDriverService.getEarningsSummary(driverId, startDate, endDate);
    }

 */
}
