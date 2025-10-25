package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class GoogleRegisterRequest {
    private String phone;
    private String action; // should be "signup"
    private String email;
    private String name;
    private String googleId;
    private String avatar;
}