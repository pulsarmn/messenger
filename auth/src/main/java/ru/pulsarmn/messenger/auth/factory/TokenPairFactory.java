package ru.pulsarmn.messenger.auth.factory;

import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.auth.dto.response.TokenPairResponse;
import ru.pulsarmn.messenger.infrastructure.jwt.JwtClaims;
import ru.pulsarmn.messenger.user.api.dto.response.UserDto;

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

    public TokenPairResponse createTokenPair(UserDto user) {
        JwtClaims jwtClaims = buildClaims(user);
        String accessToken = accessTokenFactory.createAccessToken(jwtClaims);
        String refreshToken = refreshTokenFactory.createRefreshToken(user);
        return new TokenPairResponse(accessToken, refreshToken);
    }

    private JwtClaims buildClaims(UserDto user) { // TODO: extract to JwtClaimsBuilder or JwtClaimsFactory
        return JwtClaims.builder()
                .subject(user.id().toString())
                .claim("username", user.username())
                .expirationTime(getExpirationTime())
                .issueTime(getIssueTime())
                .build();
    }

    private Instant getExpirationTime() {
        return Instant.now(clock).plus(1440, ChronoUnit.MINUTES); // TODO - extract this hard code
    }

    private Instant getIssueTime() {
        return Instant.now(clock);
    }
}
