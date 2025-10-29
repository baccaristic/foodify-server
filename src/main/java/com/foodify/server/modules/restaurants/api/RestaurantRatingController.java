package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.restaurants.application.RestaurantRatingService;
import com.foodify.server.modules.restaurants.dto.RestaurantRatingRequest;
import com.foodify.server.modules.restaurants.dto.RestaurantRatingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/restaurants")
@RequiredArgsConstructor
public class RestaurantRatingController {

    private final RestaurantRatingService restaurantRatingService;

    @PostMapping("/orders/{orderId}/ratings")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<RestaurantRatingResponse> rateRestaurant(
            @PathVariable Long orderId,
            @Valid @RequestBody RestaurantRatingRequest request,
            Authentication authentication
    ) {
        Long clientId = Long.parseLong(authentication.getPrincipal().toString());
        RestaurantRatingResponse response = restaurantRatingService.rateRestaurant(clientId, orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders/{orderId}/ratings")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<RestaurantRatingResponse> findRating(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        Long clientId = Long.parseLong(authentication.getPrincipal().toString());
        return restaurantRatingService.findRating(clientId, orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
