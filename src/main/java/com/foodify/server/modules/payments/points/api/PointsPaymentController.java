package com.foodify.server.modules.payments.points.api;

import com.foodify.server.modules.payments.points.application.PointsPaymentService;
import com.foodify.server.modules.payments.points.dto.CreatePointsPaymentRequest;
import com.foodify.server.modules.payments.points.dto.PointsPaymentResponse;
import com.foodify.server.modules.payments.points.dto.ScanPaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments/points")
@RequiredArgsConstructor
public class PointsPaymentController {

    private final PointsPaymentService pointsPaymentService;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong(authentication.getPrincipal().toString());
    }

    /**
     * Restaurant creates a payment request and gets a QR code
     */
    @PostMapping("/restaurant/create")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public ResponseEntity<PointsPaymentResponse> createPayment(
            @Valid @RequestBody CreatePointsPaymentRequest request,
            @RequestParam Long restaurantId,
            Authentication authentication) {
        PointsPaymentResponse response = pointsPaymentService.createPayment(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Client scans QR code and completes the payment
     */
    @PostMapping("/client/scan")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<PointsPaymentResponse> scanAndPay(
            @Valid @RequestBody ScanPaymentRequest request,
            Authentication authentication) {
        Long clientId = extractUserId(authentication);
        PointsPaymentResponse response = pointsPaymentService.scanAndPay(clientId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get payment history for a restaurant
     */
    @GetMapping("/restaurant/{restaurantId}/history")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public ResponseEntity<List<PointsPaymentResponse>> getRestaurantPayments(
            @PathVariable Long restaurantId,
            Authentication authentication) {
        List<PointsPaymentResponse> payments = pointsPaymentService.getRestaurantPayments(restaurantId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payment history for a client
     */
    @GetMapping("/client/history")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<PointsPaymentResponse>> getClientPayments(Authentication authentication) {
        Long clientId = extractUserId(authentication);
        List<PointsPaymentResponse> payments = pointsPaymentService.getClientPayments(clientId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get details of a specific payment
     */
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public ResponseEntity<PointsPaymentResponse> getPayment(@PathVariable Long paymentId) {
        PointsPaymentResponse payment = pointsPaymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    /**
     * Cancel a pending payment
     */
    @DeleteMapping("/restaurant/{restaurantId}/payments/{paymentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public ResponseEntity<Void> cancelPayment(
            @PathVariable Long restaurantId,
            @PathVariable Long paymentId,
            Authentication authentication) {
        pointsPaymentService.cancelPayment(restaurantId, paymentId);
        return ResponseEntity.noContent().build();
    }
}
