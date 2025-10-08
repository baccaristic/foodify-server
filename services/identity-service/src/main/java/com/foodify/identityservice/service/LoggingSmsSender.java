package com.foodify.identityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingSmsSender implements SmsSender {
    @Override
    public void sendVerificationCode(String phoneNumber, String code) {
        log.info("[identity-service] Sending verification code {} to {}", code, phoneNumber);
    }
}
