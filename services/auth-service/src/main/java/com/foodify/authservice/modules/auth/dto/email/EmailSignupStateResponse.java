package com.foodify.authservice.modules.auth.dto.email;

import com.foodify.authservice.modules.auth.dto.phone.AuthenticatedClientResponse;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;

@Value
@Builder
public class EmailSignupStateResponse {
    String sessionId;
    String email;
    boolean emailVerified;
    boolean phoneProvided;
    boolean phoneVerified;
    boolean profileProvided;
    boolean termsAccepted;
    boolean completed;
    String nextStep;
    Instant emailCodeExpiresAt;
    Instant emailResendAvailableAt;
    Integer emailAttemptsRemaining;
    Integer emailResendsRemaining;
    Instant phoneCodeExpiresAt;
    Instant phoneResendAvailableAt;
    Integer phoneAttemptsRemaining;
    Integer phoneResendsRemaining;
    String phoneNumber;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String accessToken;
    String refreshToken;
    AuthenticatedClientResponse user;
}
