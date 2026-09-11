package ru.pulsarmn.messenger.auth.dto.response;


public record TokenPairResponse(String accessToken,
                                String refreshToken) {
}
