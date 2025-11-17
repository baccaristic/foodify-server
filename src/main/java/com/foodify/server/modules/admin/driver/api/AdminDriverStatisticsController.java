package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.DriverQueryService;
import com.foodify.server.modules.admin.driver.application.DriverRatingService;
import com.foodify.server.modules.admin.driver.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/drivers/{driverId}")
@RequiredArgsConstructor
public class AdminDriverStatisticsController {

    private final DriverQueryService driverQueryService;
    private final DriverRatingService driverRatingService;

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverStatisticsDto> getDriverStatistics(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        DriverStatisticsDto statistics = driverQueryService.getDriverStatistics(driverId, startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/rating")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Double> getDriverRating(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double rating = driverRatingService.getDriverRating(driverId, startDate, endDate);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/daily-ratings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyRatingDto>> getDailyRatings(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyRatingDto> dailyRatings = driverRatingService.getDriverDailyRatings(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyRatings);
    }

    @GetMapping("/ratings-with-comments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<DriverRatingCommentDto>> getRatingsWithComments(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<DriverRatingCommentDto> dtoPage = driverRatingService.getDriverRatingsWithComments(
                driverId, startDate, endDate, page, size);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/rating-distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<RatingDistributionDto>> getRatingDistribution(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<RatingDistributionDto> distribution = driverRatingService.getDriverRatingDistribution(driverId, startDate, endDate);
        return ResponseEntity.ok(distribution);
    }
}
