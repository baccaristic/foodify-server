package com.foodify.server.modules.delivery.api;

import com.foodify.server.modules.delivery.application.DriverFinancialService;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.delivery.dto.DriverDailyFeePaymentRequest;
import com.foodify.server.modules.delivery.dto.DriverDepositAdminDto;
import com.foodify.server.modules.delivery.dto.DriverDepositPreviewDto;
import com.foodify.server.modules.delivery.dto.DriverFinancialSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverFinanceController {
    private final DriverFinancialService driverFinancialService;

    @GetMapping("/{driverId}/finance")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverFinancialSummaryDto driverFinance(@PathVariable Long driverId) {
        return driverFinancialService.getSummary(driverId);
    }

    @GetMapping("/{driverId}/finance/deposits/confirm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverDepositPreviewDto previewDriverDeposit(@PathVariable Long driverId) {
        return driverFinancialService.previewDeposit(driverId);
    }

    @GetMapping("/deposits")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<DriverDepositAdminDto> listDeposits(@RequestParam(value = "status", required = false) DriverDepositStatus status) {
        return driverFinancialService.getDepositsForAdmin(status);
    }

    @PostMapping("/{driverId}/finance/deposits/confirm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverDepositAdminDto confirmDriverDeposit(@PathVariable Long driverId, Authentication authentication) {
        Long adminId = resolveAdminId(authentication);
        return driverFinancialService.confirmCashDeposit(adminId, driverId);
    }

    @PostMapping("/deposits/{depositId}/confirm")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverDepositAdminDto confirmDeposit(@PathVariable Long depositId, Authentication authentication) {
        Long adminId = resolveAdminId(authentication);
        return driverFinancialService.confirmDeposit(adminId, depositId);
    }

    @PostMapping("/{driverId}/finance/daily-fees/pay")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverFinancialSummaryDto recordDailyFeePayment(
            @PathVariable Long driverId,
            @Valid @RequestBody DriverDailyFeePaymentRequest request
    ) {
        return driverFinancialService.recordDailyFeePayment(driverId, request.getDaysPaid());
    }

    private Long resolveAdminId(Authentication authentication) {
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        if (!(principal instanceof String principalStr)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid administrator session");
        }
        return Long.parseLong(principalStr);
    }
}
