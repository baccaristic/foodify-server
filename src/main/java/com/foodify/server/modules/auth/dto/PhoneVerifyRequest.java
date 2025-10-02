package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class PhoneVerifyRequest {
    private String phone;
    private String code;
}
