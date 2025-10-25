package com.foodify.authservice.modules.auth.application;

public interface EmailSender {
    void sendVerificationCode(String email, String code);
}
