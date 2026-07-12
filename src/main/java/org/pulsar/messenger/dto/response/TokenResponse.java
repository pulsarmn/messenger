package org.pulsar.messenger.dto.response;


public record TokenResponse(String accessToken,
                            String refreshToken) {
}
