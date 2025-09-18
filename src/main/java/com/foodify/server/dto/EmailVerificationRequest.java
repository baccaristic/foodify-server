package com.foodify.server.dto;

import lombok.Data;

@Data
public class EmailVerificationRequest {
    private String email;
    private String code;
}
