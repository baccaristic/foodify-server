package com.foodify.authservice.modules.auth.dto.email;

import lombok.Data;

@Data
public class CaptureProfileRequest {
    private String sessionId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
