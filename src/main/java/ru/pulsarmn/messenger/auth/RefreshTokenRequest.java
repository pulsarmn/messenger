package ru.pulsarmn.messenger.auth;

import jakarta.validation.constraints.NotBlank;


public record RefreshTokenRequest(

        @NotBlank
        String oldRefreshToken
) {
}
