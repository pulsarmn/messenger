package org.pulsar.messenger.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.common.AccessTokenFactory;
import org.pulsar.messenger.common.JwtClaims;
import org.pulsar.messenger.common.RefreshTokenFactory;
import org.pulsar.messenger.dto.response.TokenResponse;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.repository.RefreshTokenRepository;
import org.springframework.stereotype.Component;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class TokenPairGenerator {

    private final Clock clock;
    private final TokenGenerator tokenGenerator;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse createResponse(User user) {
        JwtClaims claims = getClaims(user.getUsername());
        String accessToken = accessTokenFactory.createAccessToken(claims);
        String refreshToken = createRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    private String createRefreshToken(User user) {
        byte[] refreshTokenBytes = tokenGenerator.generate(32);
        RefreshToken refreshToken = refreshTokenFactory.buildRefreshToken(refreshTokenBytes, user);
        refreshTokenRepository.saveAndFlush(refreshToken);
        return convertTokenBytes(refreshTokenBytes);
    }

    private JwtClaims getClaims(String username) {
        return JwtClaims.builder()
                .subject(username)
                .expirationTime(getAccessTokenExpirationTime())
                .issueTime(getAccessTokenIssueTime())
                .build();
    }

    private Instant getAccessTokenExpirationTime() {
        return Instant.now(clock).plus(15, ChronoUnit.MINUTES);
    }

    private Instant getAccessTokenIssueTime() {
        return Instant.now(clock);
    }

    private String convertTokenBytes(byte[] tokenBytes) {
        return Base64.getUrlEncoder().encodeToString(tokenBytes);
    }
}
