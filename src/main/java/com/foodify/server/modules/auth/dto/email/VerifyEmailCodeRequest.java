package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class VerifyEmailCodeRequest {
    private String sessionId;
    private String code;
}
