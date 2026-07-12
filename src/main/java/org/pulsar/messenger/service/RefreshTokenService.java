package org.pulsar.messenger.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.BadCredentialsException;
import org.pulsar.messenger.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken find(String rawRefreshToken) {
        String refreshTokenHash = convertToHash(rawRefreshToken);
        return refreshTokenRepository.findByTokenHash(refreshTokenHash)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

    private String convertToHash(String rawRefreshToken) {
        byte[] refreshTokenBytes = Base64.getUrlDecoder().decode(rawRefreshToken.getBytes(StandardCharsets.UTF_8));
        return hashService.hash(refreshTokenBytes);
    }

    public void checkExpiration(RefreshToken refreshToken) {
        if (isExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadCredentialsException("Refresh token has expired");
        }
    }

    private boolean isExpired(RefreshToken refreshToken) {
        Instant currentTime = Instant.now(clock);
        return refreshToken.getExpiresAt().isBefore(currentTime);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
