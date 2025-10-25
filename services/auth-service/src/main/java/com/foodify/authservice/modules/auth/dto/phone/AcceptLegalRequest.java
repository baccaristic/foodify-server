package com.foodify.authservice.modules.auth.dto.phone;

import lombok.Data;

@Data
public class AcceptLegalRequest {
    private String sessionId;
    private boolean accepted;
}
