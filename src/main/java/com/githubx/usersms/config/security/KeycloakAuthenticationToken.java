package com.githubx.usersms.config.security;

import com.githubx.usersms.dto.response.TokenResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

/**
 * Authenticated token that carries the Keycloak RPT response so the login
 * success handler can write it directly to the HTTP response.
 */
public class KeycloakAuthenticationToken extends AbstractAuthenticationToken {

    private final String username;
    private final TokenResponse tokenResponse;

    public KeycloakAuthenticationToken(String username, TokenResponse tokenResponse) {
        super(List.of());
        this.username = username;
        this.tokenResponse = tokenResponse;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return tokenResponse.accessToken();
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }
}
