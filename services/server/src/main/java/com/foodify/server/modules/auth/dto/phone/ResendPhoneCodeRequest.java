package com.foodify.server.modules.auth.dto.phone;

import lombok.Data;

@Data
public class ResendPhoneCodeRequest {
    private String sessionId;
}
