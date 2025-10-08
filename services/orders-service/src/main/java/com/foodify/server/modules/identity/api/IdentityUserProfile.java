package com.foodify.server.modules.identity.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record IdentityUserProfile(
        Long id,
        String email,
        String name,
        String role,
        String phone,
        String address,
        Boolean emailVerified,
        Boolean phoneVerified,
        Boolean verified,
        String googleId
) {
    public IdentityUserProfile {
        if (verified == null) {
            verified = Boolean.TRUE.equals(emailVerified) && Boolean.TRUE.equals(phoneVerified);
        }
    }
}
