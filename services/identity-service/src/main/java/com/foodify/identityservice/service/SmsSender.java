package com.foodify.identityservice.service;

public interface SmsSender {
    void sendVerificationCode(String phoneNumber, String code);
}
