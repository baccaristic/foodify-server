package com.foodify.authservice.modules.auth.application;

public interface SmsSender {
    void sendVerificationCode(String phoneNumber, String code);
}
