package com.foodify.server.modules.auth.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingSmsSender implements SmsSender {
    @Override
    public void sendVerificationCode(String phoneNumber, String code) {
        log.info("Simulated SMS sent to {} with verification code {}", phoneNumber, code);
    }
}
