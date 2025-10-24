package com.foodify.server.modules.auth.dto.phone;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;

@Value
@Builder
public class PhoneSignupStateResponse {
    String sessionId;
    String phoneNumber;
    boolean phoneVerified;
    boolean emailProvided;
    boolean emailVerified;
    boolean nameProvided;
    boolean termsAccepted;
    boolean completed;
    String nextStep;
    Instant codeExpiresAt;
    Instant resendAvailableAt;
    Integer attemptsRemaining;
    Integer resendsRemaining;
    Instant emailCodeExpiresAt;
    Instant emailResendAvailableAt;
    Integer emailAttemptsRemaining;
    Integer emailResendsRemaining;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    boolean loginAttempt;
    String accessToken;
    String refreshToken;
    AuthenticatedClientResponse user;
}
