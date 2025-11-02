package com.foodify.server.modules.orders.fee.api;

import com.foodify.server.modules.orders.fee.application.ServiceFeeService;
import com.foodify.server.modules.orders.fee.dto.ServiceFeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-fee")
@RequiredArgsConstructor
public class ServiceFeeController {

    private final ServiceFeeService serviceFeeService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN','ROLE_RESTAURANT_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<ServiceFeeResponse> getServiceFee() {
        return ResponseEntity.ok(ServiceFeeResponse.fromSetting(serviceFeeService.getCurrentSetting()));
    }
}
