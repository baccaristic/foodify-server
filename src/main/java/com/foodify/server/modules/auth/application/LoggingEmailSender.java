package com.foodify.server.modules.auth.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
@Primary
public class LoggingEmailSender implements EmailSender {
    @Override
    public void sendVerificationCode(String email, String code) {
        log.info("[DEV] Simulated email sent to {} with verification code {}", email, code);
    }
}