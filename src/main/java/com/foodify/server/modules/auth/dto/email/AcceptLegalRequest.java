package com.foodify.server.modules.auth.dto.email;

import lombok.Data;

@Data
public class AcceptLegalRequest {
    private String sessionId;
    private boolean accepted;
}
