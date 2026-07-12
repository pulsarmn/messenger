package org.pulsar.messenger.common;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.service.HashService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
@RequiredArgsConstructor
public class RefreshTokenFactory {

    private final HashService hashService;

    public RefreshToken buildRefreshToken(byte[] tokenBytes, User user) {
        String refreshTokenHash = hashService.hash(tokenBytes);
        return RefreshToken.builder()
                .tokenHash(refreshTokenHash)
                .user(user)
                .expiresAt(getRefreshTokenExpirationTime())
                .build();
    }

    private Instant getRefreshTokenExpirationTime() {
        return Instant.now().plus(60, ChronoUnit.DAYS);
    }
}
