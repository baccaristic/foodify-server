package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class UpdateDriverProfileRequest {
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;
}
