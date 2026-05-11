package com.githubx.usersms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank @JsonProperty("refresh_token") String refreshToken
) {}
