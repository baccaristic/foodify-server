package com.foodify.server.modules.auth.application;

public interface EmailSender {
    void sendVerificationCode(String email, String code);
}
