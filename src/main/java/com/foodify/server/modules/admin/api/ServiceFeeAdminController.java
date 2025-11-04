package com.foodify.server.modules.admin.api;

import com.foodify.server.modules.admin.application.AdminServiceFeeService;
import com.foodify.server.modules.admin.dto.fee.ServiceFeeResponse;
import com.foodify.server.modules.admin.dto.fee.UpdateServiceFeeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/service-fee")
@RequiredArgsConstructor
public class ServiceFeeAdminController {

    private final AdminServiceFeeService serviceFeeService;

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceFeeResponse> updateServiceFee(@Valid @RequestBody UpdateServiceFeeRequest request,
                                                               Authentication authentication) {
        String updatedBy = authentication != null && authentication.getPrincipal() != null
                ? authentication.getPrincipal().toString()
                : "system";
        return ResponseEntity.ok(ServiceFeeResponse.fromSetting(
                serviceFeeService.updateServiceFee(request.amount(), updatedBy)
        ));
    }
}
