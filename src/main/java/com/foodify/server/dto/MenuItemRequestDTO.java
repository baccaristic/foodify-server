package com.foodify.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemRequestDTO {
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean popular;
    private Long restaurantId;
    private List<ExtraDTO> extras;

    // Getters and Setters
}