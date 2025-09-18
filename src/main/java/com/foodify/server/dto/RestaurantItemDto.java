package com.foodify.server.dto;

import com.foodify.server.models.MenuItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantItemDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String address;
    private String phone;
    private String type;
    private String openingHours;
    private String closingHours;
    private String rating;
    private double latitude;
    private double longitude;
    private List<MenuItem> menu;
}
