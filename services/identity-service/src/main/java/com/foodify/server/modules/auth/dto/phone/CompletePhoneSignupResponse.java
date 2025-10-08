package com.foodify.server.modules.auth.dto.phone;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompletePhoneSignupResponse {
    PhoneSignupStateResponse state;
    String accessToken;
    String refreshToken;
    AuthenticatedClientResponse user;
    String tokenType;
    Long expiresIn;
    String scope;
}
