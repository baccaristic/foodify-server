package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RestaurantDisplayDto {
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private Set<RestaurantCategory> categories;
    private String address;
    private String phone;
    private String openingHours;
    private String imageUrl;
    private String iconUrl;
    private String closingHours;
    private double latitude;
    private double longitude;
    private boolean favorite;
    private Double deliveryFee;
    private boolean hasPromotion;
    private String promotionSummary;
}
