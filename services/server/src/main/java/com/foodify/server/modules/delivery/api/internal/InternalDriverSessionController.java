package com.foodify.server.modules.delivery.api.internal;

import com.foodify.server.modules.delivery.application.DriverSessionService;
import com.foodify.server.modules.delivery.domain.DriverSession;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/internal/driver-sessions")
@RequiredArgsConstructor
public class InternalDriverSessionController {

    private final DriverRepository driverRepository;
    private final DriverSessionService driverSessionService;

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> startSession(@RequestBody StartDriverSessionRequest request) {
        if (request.driverId() == null || !StringUtils.hasText(request.deviceId())) {
            throw new ResponseStatusException(BAD_REQUEST, "Driver ID and device ID are required");
        }

        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Driver not found"));

        DriverSession session = driverSessionService.startSession(driver, request.deviceId());
        return ResponseEntity.ok(Map.of("sessionToken", session.getSessionToken()));
    }

    public record StartDriverSessionRequest(@NotNull Long driverId, @NotBlank String deviceId) {}
}
