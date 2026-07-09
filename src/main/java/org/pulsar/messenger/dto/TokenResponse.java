package org.pulsar.messenger.dto;


public record TokenResponse(String accessToken,
                            String refreshToken) {
}
