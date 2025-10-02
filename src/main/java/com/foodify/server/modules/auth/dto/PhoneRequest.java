package com.foodify.server.modules.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PhoneRequest {
    private String phone;
}