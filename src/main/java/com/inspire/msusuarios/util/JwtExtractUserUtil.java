package com.inspire.msusuarios.util;

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
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Jwt principal = (Jwt) authentication.getPrincipal();
            return principal.getClaimAsString("user_db_id");
        }
        return null;
    }
}
