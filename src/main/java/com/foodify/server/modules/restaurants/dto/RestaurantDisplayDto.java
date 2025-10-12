package com.foodify.server.modules.restaurants.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDisplayDto {
    private Long id;
    private  String name;
    private String description;
    private Double rating;
    private String type;
    private String address;
    private String phone;
    private String openingHours;
    private String imageUrl;
    private String closingHours;
    private double latitude;
    private double longitude;
    private boolean favorite;
    private Double deliveryFee;
    private boolean hasPromotion;
    private String promotionSummary;
}
