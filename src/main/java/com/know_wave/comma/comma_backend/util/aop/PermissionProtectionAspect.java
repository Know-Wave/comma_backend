package com.know_wave.comma.comma_backend.util.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.PERMISSION_DENIED;

@Aspect
public class PermissionProtectionAspect {

    @Before("@annotation(com.know_wave.comma.comma_backend.util.annotation.PermissionProtection)")
    public void checkPermission() throws AccessDeniedException {
        if (!requestHasPermission()) {
            throw new AuthorizationServiceException(PERMISSION_DENIED);
        }
    }

    private boolean requestHasPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                            || authority.getAuthority().equals("ROLE_MANAGER"));
        }

        return false;
    }
}
