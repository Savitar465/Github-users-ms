package com.githubx.usersms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githubx.usersms.dto.request.LoginRequest;
import com.githubx.usersms.dto.response.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Map;

/**
 * Intercepts POST /v1/auth/login, reads a JSON LoginRequest from the body,
 * delegates to KeycloakAuthenticationProvider, and writes the TokenResponse as JSON.
 * The filter chain stops here — the request never reaches the DispatcherServlet.
 */
@Slf4j
public class KeycloakLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public KeycloakLoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(request -> "POST".equalsIgnoreCase(request.getMethod())
                && "/v1/auth/login".equals(request.getRequestURI()), authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        TokenResponse token = ((KeycloakAuthenticationToken) authResult).getTokenResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        log.warn("Login failed: {}", failed.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Map.of("error", failed.getMessage()));
    }
}
