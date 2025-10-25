package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    public String email;
    public String password;
    public String deviceId;
}
