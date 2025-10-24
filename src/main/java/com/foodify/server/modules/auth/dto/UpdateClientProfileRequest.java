package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class UpdateClientProfileRequest {
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String dateOfBirth;
}
