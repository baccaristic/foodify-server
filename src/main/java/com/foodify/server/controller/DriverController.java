package com.foodify.server.controller;

import com.foodify.server.Redis.DriverLocationService;
import com.foodify.server.dto.*;
import com.foodify.server.models.Order;
import com.foodify.server.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/order/{id}")
    @PreAuthorize(("hasAuthority('ROLE_DRIVER')"))
    public Order getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.driverService.getOrderDetails(id, userId);
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public OrderDto acceptOrder(Authentication authentication, @PathVariable Long id) throws Exception {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.driverService.acceptOrder(userId, id);

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

    @PostMapping("/pickup")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<String> markPickedUp(Authentication authentication, @RequestBody PickUpOrderRequest pickUpOrderRequest) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        Boolean result = driverService.pickUpOrder(pickUpOrderRequest, userId);
        if (result) {
            return ResponseEntity.ok("Order marked as picked up");
        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping("/ongoing-order")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public OrderDto ongoingOrder(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return driverService.getOngoingOrder(userId);
    }

    @PostMapping("/deliver-order")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public Boolean deliverOrder(Authentication authentication, @RequestBody DeliverOrderDto request) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.driverService.deliverOrder(userId, request);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public List<OrderDto> getHisotry(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.driverService.getOrderHistory(userId);
    }
}
