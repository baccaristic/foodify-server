package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompleteRegistrationResponse(
        @JsonProperty("user") IdentityUserProfile user,
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("refreshToken") String refreshToken,
        @JsonProperty("tokenType") String tokenType,
        @JsonProperty("expiresIn") long expiresIn,
        @JsonProperty("scope") String scope
) {
}
