package com.foodify.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraDTO {
    private Long id;
    private String name;
    private double price;
    private boolean required;
}
