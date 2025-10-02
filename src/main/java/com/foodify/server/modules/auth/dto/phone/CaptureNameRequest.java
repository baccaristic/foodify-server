package com.foodify.server.modules.auth.dto.phone;

import lombok.Data;

@Data
public class CaptureNameRequest {
    private String sessionId;
    private String firstName;
    private String lastName;
}
