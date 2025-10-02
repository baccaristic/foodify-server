package com.foodify.server.modules.restaurants.dto;

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

    // Multiple images
    private List<String> imageUrls;

    // Groups like "Choose your toppings", "Choose your meat"
    private List<OptionGroupDTO> optionGroups;
}