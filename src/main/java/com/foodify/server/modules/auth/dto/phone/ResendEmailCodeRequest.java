package com.foodify.server.modules.auth.dto.phone;

import lombok.Data;

@Data
public class ResendEmailCodeRequest {
    private String sessionId;
}
