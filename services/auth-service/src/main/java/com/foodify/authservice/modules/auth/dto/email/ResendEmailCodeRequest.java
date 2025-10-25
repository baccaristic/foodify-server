package com.foodify.authservice.modules.auth.dto.email;

import lombok.Data;

@Data
public class ResendEmailCodeRequest {
    private String sessionId;
}
