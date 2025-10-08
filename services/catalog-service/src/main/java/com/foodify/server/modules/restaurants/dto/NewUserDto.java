package com.foodify.server.modules.restaurants.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private String name;
    private String email;
    private String password;
}
