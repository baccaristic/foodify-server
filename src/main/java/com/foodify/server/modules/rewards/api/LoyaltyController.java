package com.foodify.server.modules.rewards.api;

import com.foodify.server.modules.rewards.application.LoyaltyService;
import com.foodify.server.modules.rewards.dto.CouponDto;
import com.foodify.server.modules.rewards.dto.LoyaltyBalanceResponse;
import com.foodify.server.modules.rewards.dto.LoyaltyTransactionDto;
import com.foodify.server.modules.rewards.dto.RedeemCouponRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong(authentication.getPrincipal().toString());
    }

    @GetMapping("/points")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<LoyaltyBalanceResponse> getBalance(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(loyaltyService.getBalance(userId));
    }

    @GetMapping("/points/transactions")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<LoyaltyTransactionDto>> getTransactions(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(loyaltyService.getTransactions(userId));
    }

    @PostMapping("/coupons/redeem")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<CouponDto> redeemCoupon(@Valid @RequestBody RedeemCouponRequest request,
                                                  Authentication authentication) {
        Long userId = extractUserId(authentication);
        CouponDto coupon = loyaltyService.redeemCouponWithPoints(userId, request);
        return ResponseEntity.ok(coupon);
    }
}
