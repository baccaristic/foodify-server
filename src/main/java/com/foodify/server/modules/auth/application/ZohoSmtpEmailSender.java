package com.foodify.server.modules.auth.application;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Profile("!dev")
@RequiredArgsConstructor
public class ZohoSmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    @Value("${mail.smtp.from:${spring.mail.username:no-reply@foodifytn.app}}")
    private String fromAddress;

    @Value("${mail.smtp.subject:Your Foodify verification code}")
    private String subject;

    @Value("${mail.verification-template:classpath:templates/verification-email.html}")
    private String verificationTemplateLocation;

    private String verificationTemplate;
    private boolean templateIsHtml;

    private static final String CODE_PLACEHOLDER = "123456";

    @PostConstruct
    void loadTemplate() {
        verificationTemplate = readTemplate();
        templateIsHtml = isHtmlTemplate(verificationTemplateLocation, verificationTemplate);
    }

    @Override
    public void sendVerificationCode(String email, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(buildBody(code), templateIsHtml);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException ex) {
            log.error("Failed to send verification email to {}", email, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to send verification email. Please try again later.");
        }
    }

    private String buildBody(String code) {
        if (verificationTemplate == null) {
            return String.format("Your Foodify verification code is %s", code);
        }

        if (!verificationTemplate.contains(CODE_PLACEHOLDER)) {
            return verificationTemplate + System.lineSeparator() + System.lineSeparator() + code;
        }

        return verificationTemplate.replace(CODE_PLACEHOLDER, code);
    }

    private String readTemplate() {
        if (verificationTemplateLocation == null || verificationTemplateLocation.isBlank()) {
            return null;
        }

        Resource resource = resourceLoader.getResource(verificationTemplateLocation);
        if (!resource.exists()) {
            log.warn("Verification email template {} was not found. Falling back to plain text body.", verificationTemplateLocation);
            return null;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            String template = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            if (!template.contains(CODE_PLACEHOLDER)) {
                log.warn("Verification email template {} does not contain placeholder {}.", verificationTemplateLocation, CODE_PLACEHOLDER);
            }
            return template;
        } catch (IOException ex) {
            log.error("Unable to load verification template from {}", verificationTemplateLocation, ex);
            return null;
        }
    }

    private boolean isHtmlTemplate(String location, String template) {
        if (template == null) {
            return false;
        }

        if (location != null) {
            String lowerLocation = location.toLowerCase(Locale.ROOT);
            if (lowerLocation.endsWith(".html") || lowerLocation.endsWith(".htm")) {
                return true;
            }
        }

        String normalized = template.trim().toLowerCase(Locale.ROOT);
        return normalized.startsWith("<!doctype") || normalized.startsWith("<html") || normalized.contains("<body");
    }
}
