package com.foodify.server.modules.notifications.api;

import com.foodify.server.modules.notifications.dto.DeviceRegisterRequest;
import com.foodify.server.modules.notifications.dto.DeviceUnregisterRequest;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class UserDeviceController {

    private final UserDeviceService deviceService;

    public UserDeviceController(UserDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(
            Authentication authentication,
            @RequestBody DeviceRegisterRequest request) {
        deviceService.registerDevice(Long.parseLong((String) authentication.getPrincipal()), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unregister")
    public ResponseEntity<?> unregisterDevice(
            Authentication authentication,
            @RequestBody DeviceUnregisterRequest request) {
        deviceService.unregisterDevice(Long.parseLong((String) authentication.getPrincipal()), request);
        return ResponseEntity.ok().build();
    }
}

