package ru.pulsarmn.messenger.auth.dto.request;

import jakarta.validation.constraints.NotBlank;


public record RefreshTokenRequest(@NotBlank String oldRefreshToken) {
}
