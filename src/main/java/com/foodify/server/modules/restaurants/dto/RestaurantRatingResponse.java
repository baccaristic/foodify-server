package com.foodify.server.modules.restaurants.dto;

import java.time.LocalDateTime;

public record RestaurantRatingResponse(
        Long id,
        Long restaurantId,
        Long orderId,
        Long clientId,
        Boolean thumbsUp,
        String comments,
        Double restaurantAverageRating,
        Long totalRatings,
        Long thumbsUpCount,
        Long thumbsDownCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
