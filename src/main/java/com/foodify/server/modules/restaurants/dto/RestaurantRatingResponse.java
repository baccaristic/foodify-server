package com.foodify.server.modules.restaurants.dto;

import java.time.LocalDateTime;

public record RestaurantRatingResponse(
        Long id,
        Long restaurantId,
        Long orderId,
        Long clientId,
        Integer rating,
        String comments,
        Double restaurantAverageRating,
        Long totalRatings,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
