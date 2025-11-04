package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class AdminRestaurantListItemDto {
    Long id;
    String name;
    String address;
    Set<String> cuisines; // Categories
    Boolean isOpen;
    Double overallRating;
    String imageUrl;
}
