package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
