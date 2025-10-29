package com.foodify.server.modules.delivery.api;

import com.foodify.server.modules.delivery.application.DeliveryRatingService;
import com.foodify.server.modules.delivery.dto.DeliveryRatingRequest;
import com.foodify.server.modules.delivery.dto.DeliveryRatingResponse;
import com.foodify.server.modules.delivery.dto.DriverRatingSummaryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverRatingController {

    private final DeliveryRatingService deliveryRatingService;

    @GetMapping("/{driverId}/ratings/summary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverRatingSummaryDto getSummary(@PathVariable Long driverId) {
        return deliveryRatingService.getDriverSummary(driverId);
    }

    @GetMapping("/{driverId}/ratings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<DeliveryRatingResponse> getRatings(@PathVariable Long driverId,
                                                   @RequestParam(value = "limit", defaultValue = "20") int limit) {
        return deliveryRatingService.getRatingsForDriver(driverId, limit);
    }

    @PostMapping("/orders/{orderId}/ratings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DeliveryRatingResponse> overrideRating(@PathVariable Long orderId,
                                                                 @Valid @RequestBody DeliveryRatingRequest request) {
        DeliveryRatingResponse response = deliveryRatingService.upsertRatingForAdmin(orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
