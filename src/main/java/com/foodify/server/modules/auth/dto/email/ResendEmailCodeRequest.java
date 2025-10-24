package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class ResendEmailCodeRequest {
    private String sessionId;
}
