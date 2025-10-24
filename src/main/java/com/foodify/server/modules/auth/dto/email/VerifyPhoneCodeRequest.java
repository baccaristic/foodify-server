package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class VerifyPhoneCodeRequest {
    private String sessionId;
    private String code;
}
