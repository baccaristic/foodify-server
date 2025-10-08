package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleRegisterResponse(
        String email,
        String name,
        String googleId,
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("refreshToken") String refreshToken
) {
}
