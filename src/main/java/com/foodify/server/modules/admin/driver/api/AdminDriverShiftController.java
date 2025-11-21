package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.DriverShiftService;
import com.foodify.server.modules.admin.driver.dto.AdminDriverShiftDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/drivers/{driverId}/shifts")
@RequiredArgsConstructor
public class AdminDriverShiftController {

    private final DriverShiftService driverShiftService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminDriverShiftDto>> getDriverShifts(
            @PathVariable Long driverId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminDriverShiftDto> shifts = driverShiftService.getDriverShifts(driverId, date, page, size);
        return ResponseEntity.ok(shifts);
    }
}
