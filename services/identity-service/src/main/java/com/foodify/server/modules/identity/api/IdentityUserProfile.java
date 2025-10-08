package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdentityUserProfile(
        @JsonProperty("id") Long id,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name,
        @JsonProperty("role") String role,
        @JsonProperty("phone") String phone,
        @JsonProperty("address") String address,
        @JsonProperty("emailVerified") Boolean emailVerified,
        @JsonProperty("phoneVerified") Boolean phoneVerified,
        @JsonProperty("avatarUrl") String avatarUrl,
        @JsonProperty("googleId") String googleId
) {
}
