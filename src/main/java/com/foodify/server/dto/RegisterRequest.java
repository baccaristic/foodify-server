package com.foodify.server.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phone;
    private String email;
    private String password;
    private String name;
    private String address;
    private boolean phoneVerified;
    private boolean emailVerified;
    private boolean googleAccount;
}
