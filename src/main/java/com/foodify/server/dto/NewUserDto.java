package com.foodify.server.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private String name;
    private String email;
    private String password;
}
