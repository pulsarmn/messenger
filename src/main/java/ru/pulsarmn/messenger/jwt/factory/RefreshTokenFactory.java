package ru.pulsarmn.messenger.jwt.factory;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.entity.RefreshToken;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.jwt.RefreshTokenGenerator;
import ru.pulsarmn.messenger.service.HashService;
import ru.pulsarmn.messenger.service.RefreshTokenService;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;


@Component
public class RefreshTokenFactory {

    private final Clock clock;
    private final HashService hashService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenGenerator refreshTokenGenerator;

    private static final int DEFAULT_REFRESH_TOKEN_LENGTH = 32;

    public RefreshTokenFactory(Clock clock, HashService hashService, RefreshTokenService refreshTokenService, RefreshTokenGenerator refreshTokenGenerator) {
        this.clock = clock;
        this.hashService = hashService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public String createRefreshToken(User user) {
        byte[] rawRefreshTokenBytes = refreshTokenGenerator.generate(DEFAULT_REFRESH_TOKEN_LENGTH);
        RefreshToken refreshToken = buildRefreshToken(rawRefreshTokenBytes, user);
        refreshTokenService.save(refreshToken);
        return Base64.getUrlEncoder().encodeToString(rawRefreshTokenBytes);
    }

    private RefreshToken buildRefreshToken(byte[] rawRefreshTokenBytes, User user) {
        byte[] hashedRefreshTokenBytes = hashService.hash(rawRefreshTokenBytes);
        String hashedRefreshToken = convertHashToHex(hashedRefreshTokenBytes);
        return RefreshToken.builder()
                .tokenHash(hashedRefreshToken)
                .user(user)
                .expiresAt(getRefreshTokenExpirationTime())
                .build();
    }

    private String convertHashToHex(byte[] hashBytes) {
        return HexFormat.of().formatHex(hashBytes);
    }

    private Instant getRefreshTokenExpirationTime() {
        return Instant.now(clock).plus(30, ChronoUnit.DAYS);
    }
}
