package com.foodify.server.modules.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private boolean googleAccount;
    private boolean phoneVerified;
    private boolean emailVerified;
}
