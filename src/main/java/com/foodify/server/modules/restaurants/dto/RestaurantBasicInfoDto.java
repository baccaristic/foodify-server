package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantBasicInfoDto {
    private Long id;
    private String name;
    private String nameEn;
    private String nameFr;
    private String nameAr;
    private String description;
    private String descriptionEn;
    private String descriptionFr;
    private String descriptionAr;
    private Double rating;
    private String imageUrl;
    private String iconUrl;
    private List<String> images;
    private String address;
    private String phone;
    private String licenseNumber;
    private String taxId;
    private double latitude;
    private double longitude;
}
