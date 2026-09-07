package ru.pulsarmn.messenger.auth;


public record TokenPairResponse(String accessToken,
                                String refreshToken) {
}
