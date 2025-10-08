package com.foodify.server.modules.auth.dto.phone;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class PhoneSignupStateResponse {
    String sessionId;
    String phoneNumber;
    boolean phoneVerified;
    boolean emailProvided;
    boolean nameProvided;
    boolean termsAccepted;
    boolean completed;
    String nextStep;
    Instant codeExpiresAt;
    Instant resendAvailableAt;
    Integer attemptsRemaining;
    Integer resendsRemaining;
    String email;
    String firstName;
    String lastName;
    boolean loginAttempt;
    String accessToken;
    String refreshToken;
    AuthenticatedClientResponse user;
    String tokenType;
    Long expiresIn;
    String scope;
}
