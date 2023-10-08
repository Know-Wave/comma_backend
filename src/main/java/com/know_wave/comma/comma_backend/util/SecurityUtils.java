package com.know_wave.comma.comma_backend.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getAuthenticatedId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        return name;
    }
}
