package com.foodify.authservice.modules.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "phone_signup_sessions")
@Getter
@Setter
public class PhoneSignupSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String sessionId;

    @Column(nullable = false, length = 32)
    private String phoneNumber;

    @Column(nullable = false, length = 12)
    private String verificationCode;

    @Column(nullable = false)
    private Instant codeExpiresAt;

    @Column(nullable = false)
    private Instant lastCodeSentAt;

    @Column(nullable = false)
    private int failedAttemptCount = 0;

    @Column(nullable = false)
    private int resendCount = 0;

    private Instant phoneVerifiedAt;

    @Column(length = 160)
    private String email;

    private Instant emailCapturedAt;

    @Column(length = 6)
    private String emailVerificationCode;

    private Instant emailCodeExpiresAt;

    private Instant emailLastCodeSentAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int emailFailedAttemptCount = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int emailResendCount = 0;

    private Instant emailVerifiedAt;

    @Column(length = 80)
    private String firstName;

    @Column(length = 80)
    private String lastName;

    private Instant nameCapturedAt;

    private LocalDate dateOfBirth;

    private Instant termsAcceptedAt;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }

    public boolean isPhoneVerified() {
        return phoneVerifiedAt != null;
    }

    public boolean isEmailProvided() {
        return email != null && !email.isBlank();
    }

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public boolean isNameProvided() {
        boolean hasFirstName = firstName != null && !firstName.isBlank();
        boolean hasLastName = lastName != null && !lastName.isBlank();
        return hasFirstName && hasLastName && dateOfBirth != null;
    }

    public boolean isTermsAccepted() {
        return termsAcceptedAt != null;
    }
}
