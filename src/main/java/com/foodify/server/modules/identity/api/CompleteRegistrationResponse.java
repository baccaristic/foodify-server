package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompleteRegistrationResponse(
        @JsonProperty("user") IdentityUserProfile user,
        @JsonProperty("token") String token
) {
}
