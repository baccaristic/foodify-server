package com.foodify.authservice.modules.auth.dto.email;

import lombok.Data;

@Data
public class CapturePhoneRequest {
    private String sessionId;
    private String phoneNumber;
}
