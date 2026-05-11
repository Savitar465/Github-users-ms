package com.githubx.usersms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githubx.usersms.dto.request.LogoutRequest;
import com.githubx.usersms.service.contratos.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Revokes the Keycloak session by reading the refresh_token from the JSON request body
 * and calling the Keycloak logout endpoint via AuthService.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakLogoutHandler implements LogoutHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            LogoutRequest logoutRequest = objectMapper.readValue(request.getInputStream(), LogoutRequest.class);
            authService.logout(logoutRequest.refreshToken());
        } catch (IOException e) {
            log.error("Failed to read logout request body: {}", e.getMessage());
        }
    }
}
