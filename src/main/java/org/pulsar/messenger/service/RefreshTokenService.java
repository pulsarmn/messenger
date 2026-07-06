package org.pulsar.messenger.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final HashService hashService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int REFRESH_TOKEN_LENGTH = 32;

    @Transactional
    public String create(User user) {
        byte[] refreshTokenBytes = generateRandomBytes();
        String hashedRefreshToken = hashService.hash(refreshTokenBytes);
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashedRefreshToken)
                .user(user)
                .expiresAt(getExpirationTime())
                .build();
        refreshTokenRepository.save(refreshToken);
        return Base64.getUrlEncoder().encodeToString(refreshTokenBytes);
    }

    private Instant getExpirationTime() {
        return Instant.now().plus(1, ChronoUnit.MONTHS);
    }

    private byte[] generateRandomBytes() {
        byte[] refreshTokenBytes = new byte[REFRESH_TOKEN_LENGTH];
        secureRandom.nextBytes(refreshTokenBytes);

        return refreshTokenBytes;
    }
}
