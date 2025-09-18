package com.foodify.server.controller;

import com.foodify.server.Redis.DriverLocationService;
import com.foodify.server.dto.DriverLocationDto;
import com.foodify.server.dto.StatusUpdateRequest;
import com.foodify.server.models.Order;
import com.foodify.server.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverLocationService driverLocationService;
    private final DriverService driverService;

    @PostMapping("/location")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public void updateLocation(@RequestBody DriverLocationDto dto) {
        driverLocationService.updateDriverLocation(dto.getDriverId(), dto.getLatitude(), dto.getLongitude());
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public Order acceptOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.driverService.acceptOrder(id, userId);

    }
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public void updateStatus(Authentication authentication, @RequestBody StatusUpdateRequest request) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        if (request.getAvailable()) {
            driverLocationService.markAvailable(String.valueOf(userId));
        }
    }
    @GetMapping("/pendingOrders")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public List<Order> getPendingOrders(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return driverService.getIncommingOrders(userId);
    }
}
