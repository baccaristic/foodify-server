package com.foodify.authservice.modules.auth.dto.email;

import com.foodify.authservice.modules.auth.dto.phone.AuthenticatedClientResponse;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompleteEmailSignupResponse {
    EmailSignupStateResponse state;
    String accessToken;
    String refreshToken;
    AuthenticatedClientResponse user;
}
