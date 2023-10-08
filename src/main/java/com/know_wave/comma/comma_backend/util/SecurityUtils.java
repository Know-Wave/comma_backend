package com.know_wave.comma.comma_backend.util;

import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_AUTHENTICATED_REQUEST;

public class SecurityUtils {

    public static String getAuthenticatedId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name.isEmpty() || name.equals("anonymousUser")) {
            throw new IllegalArgumentException(NOT_AUTHENTICATED_REQUEST);
        }
        return name;
    }
}
