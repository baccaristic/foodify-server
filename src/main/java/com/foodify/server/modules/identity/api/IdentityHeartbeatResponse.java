package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record IdentityHeartbeatResponse(
        String status,
        String message
) {
}
