package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class ResendPhoneCodeRequest {
    private String sessionId;
}
