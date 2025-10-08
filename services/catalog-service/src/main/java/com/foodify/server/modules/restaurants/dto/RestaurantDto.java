package com.foodify.server.modules.restaurants.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    private NewUserDto admin;
}
