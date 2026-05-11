package com.githubx.usersms.config.security;

import com.githubx.usersms.dto.request.LoginRequest;
import com.githubx.usersms.dto.response.TokenResponse;
import com.githubx.usersms.service.contratos.AuthService;
import com.githubx.usersms.util.errorhandling.exceptions.KeycloakException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Delegates username/password authentication to Keycloak's token endpoint
 * (Resource Owner Password Credentials grant) via AuthService.
 */
@Component
@RequiredArgsConstructor
public class KeycloakAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            TokenResponse token = authService.login(new LoginRequest(username, password));
            return new KeycloakAuthenticationToken(username, token);
        } catch (KeycloakException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
