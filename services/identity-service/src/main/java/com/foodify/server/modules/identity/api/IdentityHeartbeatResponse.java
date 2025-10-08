package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdentityHeartbeatResponse(
        @JsonProperty("status") String status,
        @JsonProperty("message") String message
) {
}
