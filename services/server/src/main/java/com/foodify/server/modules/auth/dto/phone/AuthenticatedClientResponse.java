package com.foodify.server.modules.auth.dto.phone;

import com.foodify.server.modules.identity.domain.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class AuthenticatedClientResponse {
    Long id;
    String name;
    String email;
    String phone;
    Boolean phoneVerified;
    Boolean emailVerified;
    LocalDate dateOfBirth;
    Role role;
}
