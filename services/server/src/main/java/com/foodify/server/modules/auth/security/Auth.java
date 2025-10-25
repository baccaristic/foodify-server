package com.foodify.server.modules.auth.security;

import com.foodify.server.modules.identity.domain.Role;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@roleChecker.hasAnyRole(authentication, #roles)")
public @interface Auth {
    Role[] value();
}
