package com.foodify.server.modules.auth.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingEmailSender implements EmailSender {
    @Override
    public void sendVerificationCode(String email, String code) {
        log.info("Simulated email sent to {} with verification code {}", email, code);
    }
}
