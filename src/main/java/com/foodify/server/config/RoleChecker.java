package com.foodify.server.config;

import com.foodify.server.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

@Component("roleChecker")
public class RoleChecker {

    public boolean hasAnyRole(Authentication authentication, Role[] roles) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        String userRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        if (userRole == null) return false;

        for (Role role : roles) {
            if (("ROLE_" + role.name()).equals(userRole)) {
                return true;
            }
        }

        return false;
    }
}
