package com.foodify.identityservice.service;

import org.springframework.util.StringUtils;

public final class PhoneNumberUtils {
    private PhoneNumberUtils() {
    }

    public static String normalize(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return phoneNumber;
        }
        String digitsOnly = phoneNumber.replaceAll("[^+0-9]", "");
        if (digitsOnly.startsWith("00")) {
            return "+" + digitsOnly.substring(2);
        }
        return digitsOnly;
    }
}
