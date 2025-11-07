package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuCategoryRequestDTO {
    private String name;
    
    // Multi-language support
    private String nameEn;
    private String nameFr;
    private String nameAr;
}
