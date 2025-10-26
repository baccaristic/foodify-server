package com.foodify.authservice.modules.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "email_signup_sessions")
@Getter
@Setter
public class EmailSignupSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String sessionId;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(nullable = false)
    private String encodedPassword;

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

    @Column(length = 32)
    private String phoneNumber;

    @Column(length = 6)
    private String phoneVerificationCode;

    private Instant phoneCodeExpiresAt;

    private Instant phoneLastCodeSentAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int phoneFailedAttemptCount = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int phoneResendCount = 0;

    private Instant phoneVerifiedAt;

    @Column(length = 80)
    private String firstName;

    @Column(length = 80)
    private String lastName;

    private LocalDate dateOfBirth;

    private Instant profileCapturedAt;

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

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public boolean isPhoneProvided() {
        return phoneNumber != null && !phoneNumber.isBlank();
    }

    public boolean isPhoneVerified() {
        return phoneVerifiedAt != null;
    }

    public boolean isProfileProvided() {
        boolean hasFirstName = firstName != null && !firstName.isBlank();
        boolean hasLastName = lastName != null && !lastName.isBlank();
        return hasFirstName && hasLastName && dateOfBirth != null;
    }

    public boolean isTermsAccepted() {
        return termsAcceptedAt != null;
    }
}
