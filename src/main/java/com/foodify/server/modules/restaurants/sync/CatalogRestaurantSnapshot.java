package com.foodify.server.modules.restaurants.sync;

public record CatalogRestaurantSnapshot(
        Long id,
        Long adminId,
        String name,
        String address,
        String phone,
        String imageUrl,
        Double latitude,
        Double longitude
) {
}
