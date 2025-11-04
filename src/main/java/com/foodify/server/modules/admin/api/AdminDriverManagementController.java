package com.foodify.server.modules.admin.api;

import com.foodify.server.modules.delivery.application.AdminDriverManagementService;
import com.foodify.server.modules.delivery.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/drivers/management")
@RequiredArgsConstructor
public class AdminDriverManagementController {

    private final AdminDriverManagementService adminDriverManagementService;

    /**
     * Get today's revenue with percentage change from yesterday
     */
    @GetMapping("/revenue/today")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TodayRevenueDto> getTodayRevenue() {
        return ResponseEntity.ok(adminDriverManagementService.getTodayRevenue());
    }

    /**
     * Get list of active drivers online
     */
    @GetMapping("/drivers/active")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ActiveDriversDto> getActiveDrivers() {
        return ResponseEntity.ok(adminDriverManagementService.getActiveDrivers());
    }

    /**
     * Get percentage of daily membership collection
     */
    @GetMapping("/membership/collection")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DailyMembershipCollectionDto> getDailyMembershipCollection() {
        return ResponseEntity.ok(adminDriverManagementService.getDailyMembershipCollection());
    }

    /**
     * Get drivers by pagination with filters
     */
    @GetMapping("/drivers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminDriverListItemDto>> getDrivers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean paymentStatus,
            @RequestParam(required = false) Boolean isOnline,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Page<AdminDriverListItemDto> drivers = adminDriverManagementService.getDriversWithFilters(
                query, paymentStatus, isOnline, page, size);
        return ResponseEntity.ok(drivers);
    }

    /**
     * Get driver rating by driver ID
     */
    @GetMapping("/{driverId}/rating")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverRatingSummaryDto> getDriverRating(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getDriverRating(driverId));
    }

    /**
     * Get total orders by driver ID
     */
    @GetMapping("/{driverId}/orders/total")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Long> getTotalOrders(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getTotalOrders(driverId));
    }

    /**
     * Get on-time orders by driver ID
     */
    @GetMapping("/{driverId}/orders/on-time")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Long> getOnTimeOrders(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getOnTimeOrders(driverId));
    }

    /**
     * Get canceled orders by driver ID
     */
    @GetMapping("/{driverId}/orders/canceled")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Long> getCanceledOrders(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getCanceledOrders(driverId));
    }

    /**
     * Get driver info details by driver ID
     */
    @GetMapping("/{driverId}/details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverDetailsDto> getDriverDetails(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getDriverDetails(driverId));
    }

    /**
     * Get current status (online/offline) by driver ID
     */
    @GetMapping("/{driverId}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> getCurrentStatus(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getCurrentStatus(driverId));
    }

    /**
     * Get current task by driver ID
     */
    @GetMapping("/{driverId}/task/current")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverCurrentTaskDto> getCurrentTask(@PathVariable Long driverId) {
        Optional<DriverCurrentTaskDto> task = adminDriverManagementService.getCurrentTask(driverId);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Get current location by driver ID
     */
    @GetMapping("/{driverId}/location/current")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverLocationResponseDto> getCurrentLocation(@PathVariable Long driverId) {
        Optional<DriverLocationResponseDto> location = adminDriverManagementService.getCurrentLocation(driverId);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Get delivery metrics by driver ID
     */
    @GetMapping("/{driverId}/metrics/delivery")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DeliveryMetricsDto> getDeliveryMetrics(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getDeliveryMetrics(driverId));
    }

    /**
     * Get monthly statistics (avg rating and on-time percentages) by driver ID
     */
    @GetMapping("/{driverId}/stats/monthly")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverMonthlyStatsDto> getMonthlyStats(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getMonthlyStats(driverId));
    }

    /**
     * Get customer ratings by pagination by driver ID
     */
    @GetMapping("/{driverId}/ratings/customer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<DeliveryRatingResponse>> getCustomerRatings(
            @PathVariable Long driverId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(adminDriverManagementService.getCustomerRatings(driverId, page, size));
    }

    /**
     * Get rating distribution (1-5 stars) by driver ID
     */
    @GetMapping("/{driverId}/ratings/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RatingDistributionDto> getRatingDistribution(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getRatingDistribution(driverId));
    }

    /**
     * Get shift history by date with orders overview by driver ID
     */
    @GetMapping("/{driverId}/shifts/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<ShiftHistoryDto>> getShiftHistory(
            @PathVariable Long driverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(adminDriverManagementService.getShiftHistory(driverId, date, page, size));
    }

    /**
     * Get today's earnings (cash, card, commission) by driver ID
     */
    @GetMapping("/{driverId}/earnings/today")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TodayEarningsDto> getTodayEarnings(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getTodayEarnings(driverId));
    }

    /**
     * Get due date of daily subscription by driver ID
     */
    @GetMapping("/{driverId}/subscription/due-date")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DailySubscriptionDueDateDto> getDailySubscriptionDueDate(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminDriverManagementService.getDailySubscriptionDueDate(driverId));
    }

    /**
     * Create a payment from a specific driver
     */
    @PostMapping("/{driverId}/payments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverDepositAdminDto> createPayment(
            @PathVariable Long driverId,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        DriverDepositAdminDto payment = adminDriverManagementService.createPayment(driverId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    /**
     * Get payment history by driver ID with pagination
     */
    @GetMapping("/{driverId}/payments/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<PaymentHistoryDto>> getPaymentHistory(
            @PathVariable Long driverId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(adminDriverManagementService.getPaymentHistory(driverId, page, size));
    }
}
