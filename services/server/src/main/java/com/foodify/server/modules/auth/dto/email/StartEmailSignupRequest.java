package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class StartEmailSignupRequest {
    private String email;
    private String password;
}
