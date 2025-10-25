package com.foodify.server.modules.restaurants.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraDTO {
    private Long id;          // optional (for update scenarios)
    private String name;      // e.g. "Cheddar ++"
    private double price;     // extra price (0 if included)
    private boolean isDefault;// optional: if preselected
}