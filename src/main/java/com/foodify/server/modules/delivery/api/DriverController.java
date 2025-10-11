package com.foodify.server.modules.delivery.api;

import com.foodify.server.modules.delivery.dto.DeliverOrderDto;
import com.foodify.server.modules.delivery.dto.DriverLocationDto;
import com.foodify.server.modules.delivery.dto.DriverShiftDto;
import com.foodify.server.modules.delivery.dto.DriverShiftBalanceDto;
import com.foodify.server.modules.delivery.dto.DriverShiftEarningDetailsDto;
import com.foodify.server.modules.delivery.dto.PickUpOrderRequest;
import com.foodify.server.modules.delivery.dto.StatusUpdateRequest;
import com.foodify.server.modules.delivery.dto.DriverEarningsSummaryDto;
import com.foodify.server.modules.delivery.dto.DriverShiftIncomeResponseDto;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.delivery.application.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DriverLocationService driverLocationService;
    private final DriverService driverService;
    private final WebSocketService webSocketService;

    @PostMapping("/location")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public void updateLocation(@RequestBody DriverLocationDto dto) {
        driverLocationService.updateDriverLocation(dto.getDriverId(), dto.getLatitude(), dto.getLongitude());
        driverService.getOrderInDelivery(dto.getDriverId())
                .filter(order -> order.getClient() != null)
                .ifPresent(order -> webSocketService.notifyClient(order.getClient().getId(), order));
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
    public ResponseEntity<?> updateStatus(Authentication authentication, @RequestBody StatusUpdateRequest request) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        if (request.getAvailable() == null) {
            return ResponseEntity.badRequest().body("Availability flag is required");
        }
        try {
            DriverShiftDto shift = driverService.updateAvailability(userId, request.getAvailable());
            return ResponseEntity.ok(shift);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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

    @GetMapping("/shift")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverShiftDto currentShift(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return driverService.getCurrentShift(userId);
    }

    @GetMapping("/shift/balance")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverShiftBalanceDto currentShiftBalance(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return driverService.getCurrentShiftBalance(userId);
    }

    @GetMapping("/earnings")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverEarningsSummaryDto earningsSummary(
            Authentication authentication,
            @RequestParam(value = "dateOn", required = false) String dateOn,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        LocalDate dateOnValue = parseDate(dateOn);
        LocalDate fromValue = parseDate(from);
        LocalDate toValue = parseDate(to);

        if (dateOnValue != null && (fromValue != null || toValue != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide either dateOn or from/to parameters, not both.");
        }

        if (fromValue != null && toValue != null && fromValue.isAfter(toValue)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"from\" must be on or before \"to\".");
        }

        return driverService.getEarningsSummary(userId, dateOnValue, fromValue, toValue);
    }

    @GetMapping("/earnings/shifts")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverShiftIncomeResponseDto earningsByShift(
            Authentication authentication,
            @RequestParam(value = "dateOn", required = false) String dateOn,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        LocalDate dateOnValue = parseDate(dateOn);
        LocalDate fromValue = parseDate(from);
        LocalDate toValue = parseDate(to);

        if (dateOnValue != null && (fromValue != null || toValue != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide either dateOn or from/to parameters, not both.");
        }

        if (fromValue != null && toValue != null && fromValue.isAfter(toValue)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"from\" must be on or before \"to\".");
        }

        return driverService.getShiftIncomeDetails(userId, dateOnValue, fromValue, toValue);
    }

    @GetMapping("/earnings/shifts/{shiftId}")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverShiftEarningDetailsDto earningsForShift(
            Authentication authentication,
            @PathVariable Long shiftId
    ) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return driverService.getShiftEarningDetails(userId, shiftId);
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

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date format. Expected dd/MM/yyyy.");
        }
    }
}
