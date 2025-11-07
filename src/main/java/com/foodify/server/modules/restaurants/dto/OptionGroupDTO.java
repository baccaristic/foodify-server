package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OptionGroupDTO {
    private Long id;          // optional (for update scenarios)
    private String name;      // e.g. "Choose your toppings"
    
    // Multi-language support
    private String nameEn;
    private String nameFr;
    private String nameAr;
    
    private int minSelect;    // min number of items
    private int maxSelect;    // max number of items
    private boolean required; // true if group must be filled

    private List<ExtraDTO> extras;  // items inside the group
}
