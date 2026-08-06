package ru.pulsarmn.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;


public record RefreshTokenRequest(

        @NotBlank
        String oldRefreshToken
) {
}
