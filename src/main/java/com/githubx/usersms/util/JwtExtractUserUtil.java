package com.githubx.usersms.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtExtractUserUtil {
    private JwtExtractUserUtil() {
        throw new UnsupportedOperationException("Esta es una clase utility y no puede ser instanciada");
    }
    public static String extractUserDbId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt jwt) {
                return jwt.getClaimAsString("user_db_id");
            }
        }
        return null;
    }
}
