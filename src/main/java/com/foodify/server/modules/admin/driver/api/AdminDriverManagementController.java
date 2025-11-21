package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.DriverQueryService;
import com.foodify.server.modules.admin.driver.dto.DriverDetailsDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverManagementController {

    private final DriverQueryService driverQueryService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<DriverListItemDto>> getDrivers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<DriverListItemDto> drivers = driverQueryService.getDriverList(query, paid, page, size);
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/{driverId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DriverDetailsDto> getDriver(@PathVariable Long driverId) {
        DriverDetailsDto driver = driverQueryService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }
}
