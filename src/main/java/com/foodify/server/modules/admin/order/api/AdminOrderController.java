package com.foodify.server.modules.admin.order.api;

import com.foodify.server.modules.admin.order.application.AdminOrderService;
import com.foodify.server.modules.admin.order.dto.*;
import com.foodify.server.modules.orders.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderDto>> getOrders(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<OrderStatus> status,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OrderDto> orders = adminOrderService.getDriverList(query, status, restaurantId, page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/stats/today")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OrderStatsDto> getTodayOrdersStats() {
        OrderStatsDto stats = adminOrderService.getTodayOrdersStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PendingOrdersStatsDto> getPendingOrdersCount() {
        PendingOrdersStatsDto stats = adminOrderService.getPendingOrdersCount();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/today/revenue")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RevenueStatsDto> getTodayTotalRevenue() {
        RevenueStatsDto stats = adminOrderService.getTodayTotalRevenue();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{orderId}/client")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDto> getClientByOrderId(@PathVariable Long orderId) {
        var clientDto = adminOrderService.getClientByOrderId(orderId);
        return ResponseEntity.ok(clientDto);
    }
}
