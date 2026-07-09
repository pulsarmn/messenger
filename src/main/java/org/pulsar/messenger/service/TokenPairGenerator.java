package org.pulsar.messenger.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.TokenResponse;
import org.pulsar.messenger.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class TokenPairGenerator {

    private final Clock clock;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse create(User user) {
        Map<String, Object> claims = getClaims(user);
        String accessToken = accessTokenGenerator.generate(claims);
        String refreshToken = refreshTokenService.create(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    private Map<String, Object> getClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        Instant currentTime = Instant.now(clock);
        claims.put("sub", user.getUsername());
        claims.put("iat", currentTime.getEpochSecond());
        claims.put("exp", currentTime.plus(15, ChronoUnit.MINUTES).getEpochSecond());
        return claims;
    }
}
