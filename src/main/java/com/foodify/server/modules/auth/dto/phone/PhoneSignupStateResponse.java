package com.foodify.server.modules.auth.dto.phone;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
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
}
