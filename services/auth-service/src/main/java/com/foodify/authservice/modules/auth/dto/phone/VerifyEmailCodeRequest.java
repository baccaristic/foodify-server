package com.foodify.authservice.modules.auth.dto.phone;

import lombok.Data;

@Data
public class VerifyEmailCodeRequest {
    private String sessionId;
    private String code;
}
