package com.githubx.usersms.service.implementacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githubx.usersms.dto.request.LoginRequest;
import com.githubx.usersms.dto.response.TokenResponse;
import com.githubx.usersms.service.contratos.AuthService;
import com.githubx.usersms.util.errorhandling.exceptions.KeycloakException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String tokenEndpoint;
    private final String logoutEndpoint;
    private final String loginClientId;
    private final String loginClientSecret;

    public AuthServiceImpl(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            @Value("${keycloak-client.server-url}") String serverUrl,
            @Value("${keycloak-client.realm}") String realm,
            @Value("${keycloak-client.login-client}") String loginClientId,
            @Value("${keycloak-client.login-client-secret}") String loginClientSecret) {
        String base = serverUrl + "/realms/" + realm + "/protocol/openid-connect";
        this.tokenEndpoint     = base + "/token";
        this.logoutEndpoint    = base + "/logout";
        this.loginClientId     = loginClientId;
        this.loginClientSecret = loginClientSecret;
        this.httpClient        = httpClient;
        this.objectMapper      = objectMapper;
    }

    @PostConstruct
    public void logConfig() {
        log.info("Keycloak token endpoint : {}", tokenEndpoint);
        log.info("Keycloak logout endpoint: {}", logoutEndpoint);
        log.info("Login client ID         : {}", loginClientId);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        String body = encode("grant_type", "password")
                + "&" + encode("client_id", loginClientId)
                + "&" + encode("client_secret", loginClientSecret)
                + "&" + encode("username", request.username())
                + "&" + encode("password", request.password());

        log.debug("Login attempt: endpoint={} client_id={} username={}", tokenEndpoint, loginClientId, request.username());

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(tokenEndpoint))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug("Keycloak login response: status={} body={}", response.statusCode(), response.body());

            if (response.statusCode() == HttpStatus.OK.value()) {
                return objectMapper.readValue(response.body(), TokenResponse.class);
            }
            if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
                log.warn("Login failed for user '{}': invalid credentials. Keycloak: {}", request.username(), response.body());
                throw new KeycloakException("Invalid username or password.");
            }
            log.warn("Login failed for user '{}': status={} keycloak_response={}", request.username(), response.statusCode(), response.body());
            throw new KeycloakException("Authentication failed: " + response.body());

        } catch (KeycloakException e) {
            throw e;
        } catch (Exception e) {
            log.error("Login unexpected error for user '{}': {}", request.username(), e.getMessage());
            throw new KeycloakException("Authentication failed, please try again later.");
        }
    }

    @Override
    public void logout(String refreshToken) {
        String body = encode("client_id", loginClientId)
                + "&" + encode("client_secret", loginClientSecret)
                + "&" + encode("refresh_token", refreshToken);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(logoutEndpoint))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug("Keycloak logout response: status={} body={}", response.statusCode(), response.body());

            if (response.statusCode() >= 400) {
                log.error("Logout failed: status={} keycloak_response={}", response.statusCode(), response.body());
                throw new KeycloakException("Logout failed: " + response.body());
            }
            log.debug("Session revoked successfully");

        } catch (KeycloakException e) {
            throw e;
        } catch (Exception e) {
            log.error("Logout unexpected error: {}", e.getMessage());
            throw new KeycloakException("Logout failed, please try again later.");
        }
    }

    private static String encode(String key, String value) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8)
                + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
