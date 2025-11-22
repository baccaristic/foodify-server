package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.DriverDepositService;
import com.foodify.server.modules.admin.driver.dto.DriverDepositAdminDto;
import com.foodify.server.modules.common.util.AuthenticationUtil;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.delivery.dto.DriverDepositPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/drivers/{driverId}")
@RequiredArgsConstructor
public class AdminDriverDepositController {

    private final DriverDepositService driverDepositService;

    @GetMapping("/deposits")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<DriverDepositAdminDto> listDeposits(
            @PathVariable Long driverId,
            @RequestParam(value = "status", required = false) DriverDepositStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return driverDepositService.getDepositsForAdmin(driverId, status, page, size);
    }

    @GetMapping("/finance/deposits/confirm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverDepositPreviewDto previewDriverDeposit(@PathVariable Long driverId) {
        return driverDepositService.previewDeposit(driverId);
    }

    @PostMapping("/finance/deposits/confirm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverDepositAdminDto confirmDriverDeposit(@PathVariable Long driverId, Authentication authentication) {
        Long adminId = AuthenticationUtil.extractUserId(authentication);
        return driverDepositService.confirmCashDeposit(adminId, driverId);
    }
}
