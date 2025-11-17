package com.foodify.server.modules.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@UtilityClass
public class AuthenticationUtil {

    public static Long extractUserId(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Number number) {
            return number.longValue();
        }

        String identifier = principal instanceof String
                ? (String) principal
                : authentication.getName();

        if (identifier == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to resolve user session");
        }

        try {
            return Long.parseLong(identifier);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user session", ex);
        }
    }
}
