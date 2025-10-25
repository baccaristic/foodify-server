package com.foodify.authservice.modules.auth.dto.email;

import lombok.Data;

@Data
public class StartEmailSignupRequest {
    private String email;
    private String password;
}
