package org.pulsar.messenger.dto;


public record AuthResponse(String accessToken,
                           String refreshToken) {
}
