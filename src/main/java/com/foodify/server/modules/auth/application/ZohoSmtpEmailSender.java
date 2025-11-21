package com.foodify.server.modules.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Profile("!dev")
@RequiredArgsConstructor
public class ZohoSmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${mail.smtp.from:${spring.mail.username:no-reply@foodifytn.app}}")
    private String fromAddress;

    @Value("${mail.smtp.subject:Your Foodify verification code}")
    private String subject;

    @Override
    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(code);

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Failed to send verification email to {}", email, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to send verification email. Please try again later.");
        }
    }
}
