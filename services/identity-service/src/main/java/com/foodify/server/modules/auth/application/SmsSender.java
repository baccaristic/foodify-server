package com.foodify.server.modules.auth.application;

public interface SmsSender {
    void sendVerificationCode(String phoneNumber, String code);
}
