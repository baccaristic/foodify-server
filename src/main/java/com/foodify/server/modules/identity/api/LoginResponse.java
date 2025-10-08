package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("refreshToken") String refreshToken,
        @JsonProperty("user") IdentityUserProfile user
) {
}
