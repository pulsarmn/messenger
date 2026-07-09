package org.pulsar.messenger.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final Clock clock;
    private final HashService hashService;
    private final TokenGenerator tokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int REFRESH_TOKEN_LENGTH = 32;

    @Transactional
    public String create(User user) {
        Objects.requireNonNull(user, "User must not be null for refresh token creation");

        byte[] refreshTokenBytes = tokenGenerator.generate(REFRESH_TOKEN_LENGTH);
        String hashedRefreshToken = hashService.hash(refreshTokenBytes);
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashedRefreshToken)
                .user(user)
                .expiresAt(getExpirationTime())
                .build();
        refreshTokenRepository.saveAndFlush(refreshToken);
        return Base64.getUrlEncoder().encodeToString(refreshTokenBytes);
    }

    private Instant getExpirationTime() {
        return Instant.now(clock).plus(60, ChronoUnit.DAYS);
    }
}
