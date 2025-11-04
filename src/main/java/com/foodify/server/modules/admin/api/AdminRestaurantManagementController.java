package com.foodify.server.modules.admin.api;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.restaurants.application.AdminRestaurantManagementService;
import com.foodify.server.modules.restaurants.dto.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants/management")
@RequiredArgsConstructor
public class AdminRestaurantManagementController {

    private final AdminRestaurantManagementService adminRestaurantManagementService;

    /**
     * Get restaurants by pagination with filters
     */
    @GetMapping("/restaurants")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminRestaurantListItemDto>> getRestaurants(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String cuisine,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Page<AdminRestaurantListItemDto> restaurants = adminRestaurantManagementService
                .getRestaurantsWithFilters(query, cuisine, page, size);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * Get today's revenue with percentage change from yesterday
     */
    @GetMapping("/{restaurantId}/revenue/today")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RestaurantRevenueDto> getTodayRevenue(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminRestaurantManagementService.getTodayRevenue(restaurantId));
    }

    /**
     * Get total orders with percentage change from yesterday
     */
    @GetMapping("/{restaurantId}/orders/total")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RestaurantTotalOrdersDto> getTotalOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminRestaurantManagementService.getTotalOrders(restaurantId));
    }

    /**
     * Get average order value with percentage change from yesterday
     */
    @GetMapping("/{restaurantId}/orders/avg-value")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RestaurantAvgOrderValueDto> getAvgOrderValue(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminRestaurantManagementService.getAvgOrderValue(restaurantId));
    }

    /**
     * Get average rating with percentage change from yesterday
     */
    @GetMapping("/{restaurantId}/rating/avg")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RestaurantAvgRatingDto> getAvgRating(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(adminRestaurantManagementService.getAvgRating(restaurantId));
    }

    /**
     * Get revenue per day between two dates
     */
    @GetMapping("/{restaurantId}/revenue/daily")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyRevenueDto>> getRevenuePerDay(
            @PathVariable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DailyRevenueDto> revenue = adminRestaurantManagementService
                .getRevenuePerDay(restaurantId, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    /**
     * Get most selling items for the current day
     */
    @GetMapping("/{restaurantId}/items/top-selling")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TopSellingItemDto>> getTopSellingItems(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit
    ) {
        List<TopSellingItemDto> topItems = adminRestaurantManagementService
                .getTopSellingItems(restaurantId, limit);
        return ResponseEntity.ok(topItems);
    }

    /**
     * Get orders history by day with pagination and status filter
     */
    @GetMapping("/{restaurantId}/orders/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Order>> getOrdersHistory(
            @PathVariable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Page<Order> orders = adminRestaurantManagementService
                .getOrdersHistory(restaurantId, date, status, page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get menu items with pagination, availability, and category filter
     */
    @GetMapping("/{restaurantId}/items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<MenuItemListDto>> getMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Page<MenuItemListDto> items = adminRestaurantManagementService
                .getMenuItems(restaurantId, category, page, size);
        return ResponseEntity.ok(items);
    }
}
