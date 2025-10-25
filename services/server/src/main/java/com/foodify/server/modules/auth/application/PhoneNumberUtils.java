package com.foodify.server.modules.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

public final class PhoneNumberUtils {
    private PhoneNumberUtils() {
    }

    public static String normalize(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }
        String digitsOnly = phoneNumber.replaceAll("[^+0-9]", "");
        if (!digitsOnly.startsWith("+")) {
            digitsOnly = "+" + digitsOnly.replaceFirst("^0+", "");
        }
        if (digitsOnly.length() < 8 || digitsOnly.length() > 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must contain between 8 and 16 digits");
        }
        return digitsOnly;
    }
}
