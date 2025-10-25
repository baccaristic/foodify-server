package com.foodify.authservice.modules.auth.dto.phone;

import lombok.Data;

@Data
public class CaptureEmailRequest {
    private String sessionId;
    private String email;
}
