package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemRequestDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private List<Long> categoryIds;
    private boolean popular;
    private Long restaurantId;
    private Boolean available;

    private String promotionLabel;
    private Double promotionPrice;
    private boolean promotionActive;

    // Multiple images
    private List<String> imageUrls;

    // Groups like "Choose your toppings", "Choose your meat"
    private List<OptionGroupDTO> optionGroups;
}