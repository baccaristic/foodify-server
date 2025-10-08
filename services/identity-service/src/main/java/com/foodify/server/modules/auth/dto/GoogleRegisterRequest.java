package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class GoogleRegisterRequest {
    private String email;
    private String name;
    private String googleId;
    private String phone;
}
