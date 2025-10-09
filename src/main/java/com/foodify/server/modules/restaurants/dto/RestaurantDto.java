package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantDto {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String type;
    private double latitude;
    private double longitude;
    private String licenseNumber;
    private String taxId;
    private BigDecimal restaurantShareRate;

    private NewUserDto admin;
}
