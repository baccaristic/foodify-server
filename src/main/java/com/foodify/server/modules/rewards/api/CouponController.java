package com.foodify.server.modules.rewards.api;

import com.foodify.server.modules.rewards.application.CouponService;
import com.foodify.server.modules.rewards.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong(authentication.getPrincipal().toString());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<CouponDto>> getCoupons(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(couponService.getAvailableCoupons(userId));
    }
}
