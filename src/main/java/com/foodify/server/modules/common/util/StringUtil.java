package com.foodify.server.modules.common.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class StringUtil {

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
    }
}
