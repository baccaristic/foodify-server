package com.foodify.server.modules.delivery.api;

import com.foodify.server.modules.delivery.application.DeliveryRatingService;
import com.foodify.server.modules.delivery.dto.DeliveryRatingRequest;
import com.foodify.server.modules.delivery.dto.DeliveryRatingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery/ratings")
@RequiredArgsConstructor
public class DeliveryRatingController {

    private final DeliveryRatingService deliveryRatingService;

    @PostMapping("/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public DeliveryRatingResponse submitRating(Authentication authentication,
                                               @PathVariable Long orderId,
                                               @Valid @RequestBody DeliveryRatingRequest request) {
        Long clientId = Long.parseLong((String) authentication.getPrincipal());
        return deliveryRatingService.rateDelivery(clientId, orderId, request);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<DeliveryRatingResponse> getRating(Authentication authentication,
                                                            @PathVariable Long orderId) {
        Long clientId = Long.parseLong((String) authentication.getPrincipal());
        return deliveryRatingService.findRatingForOrder(clientId, orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
