package com.githubx.usersms.service.contratos;

import com.githubx.usersms.dto.request.LoginRequest;
import com.githubx.usersms.dto.response.TokenResponse;

public interface AuthService {

    /**
     * Authenticates a user against Keycloak using the Resource Owner Password Credentials grant
     * and returns an access token + refresh token.
     */
    TokenResponse login(LoginRequest request);

    /**
     * Invalidates the user's session in Keycloak by revoking the provided refresh token.
     */
    void logout(String refreshToken);
}
