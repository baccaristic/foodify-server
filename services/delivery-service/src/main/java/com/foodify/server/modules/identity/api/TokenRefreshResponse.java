package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRefreshResponse(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("tokenType") String tokenType,
        @JsonProperty("expiresIn") long expiresIn,
        @JsonProperty("scope") String scope
) {
}
