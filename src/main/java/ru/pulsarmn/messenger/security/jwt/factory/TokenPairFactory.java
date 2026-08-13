package ru.pulsarmn.messenger.security.jwt.factory;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.dto.response.TokenPairResponse;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.security.jwt.JwtClaims;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
public class TokenPairFactory {

    private final Clock clock;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;

    public TokenPairFactory(Clock clock, AccessTokenFactory accessTokenFactory, RefreshTokenFactory refreshTokenFactory) {
        this.clock = clock;
        this.accessTokenFactory = accessTokenFactory;
        this.refreshTokenFactory = refreshTokenFactory;
    }

    public TokenPairResponse createTokenPair(User user) {
        JwtClaims jwtClaims = buildClaims(user);
        String accessToken = accessTokenFactory.createAccessToken(jwtClaims);
        String refreshToken = refreshTokenFactory.createRefreshToken(user);
        return new TokenPairResponse(accessToken, refreshToken);
    }

    private JwtClaims buildClaims(User user) {
        return JwtClaims.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .expirationTime(getExpirationTime())
                .issueTime(getIssueTime())
                .build();
    }

    private Instant getExpirationTime() {
        return Instant.now(clock).plus(10, ChronoUnit.MINUTES); // TODO - extract this hard code
    }

    private Instant getIssueTime() {
        return Instant.now(clock);
    }
}
