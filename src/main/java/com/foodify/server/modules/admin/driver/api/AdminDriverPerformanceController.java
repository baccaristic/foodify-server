package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.DriverPerformanceService;
import com.foodify.server.modules.admin.driver.dto.DailyEarningDto;
import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DriverEarningsByPaymentMethodDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/drivers/{driverId}")
@RequiredArgsConstructor
public class AdminDriverPerformanceController {

    private final DriverPerformanceService driverPerformanceService;

    @GetMapping("/daily-on-time")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyOnTimePercentageDto>> getDailyOnTimePercentage(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyOnTimePercentageDto> dailyData = driverPerformanceService.getDriverDailyOnTimePercentage(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyData);
    }

    @GetMapping("/daily-earning")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyEarningDto>> getDailyEarning(
            @PathVariable Long driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DailyEarningDto> dailyData = driverPerformanceService.getDriverDailyEarning(driverId, startDate, endDate);
        return ResponseEntity.ok(dailyData);
    }

    @GetMapping("/earnings-by-payment-method")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverEarningsByPaymentMethodDto> getEarningsByPaymentMethod(
            @PathVariable Long driverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DriverEarningsByPaymentMethodDto earnings = driverPerformanceService.getDriverEarningsByPaymentMethod(driverId, date);
        return ResponseEntity.ok(earnings);
    }
}
