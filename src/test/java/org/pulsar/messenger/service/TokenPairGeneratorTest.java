package org.pulsar.messenger.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.messenger.dto.AuthResponse;
import org.pulsar.messenger.entity.User;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
public class TokenPairGeneratorTest {

    @Spy
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-07T10:00:00.00Z"), ZoneOffset.UTC);

    @Mock
    private AccessTokenGenerator accessTokenGenerator;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private TokenPairGenerator tokenPairGenerator;

    @Test
    void create_whenValidUser_shouldReturnPairOfTokens() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("correct-username")
                .displayName("correct-display-name")
                .passwordHash("correct-password-hash")
                .build();
        Map<String, Object> claims = Map.of(
                "sub", user.getUsername(),
                "iat", Instant.now(clock).getEpochSecond(),
                "exp", Instant.now(clock).plus(15, ChronoUnit.MINUTES).getEpochSecond()
        );
        String expectedAccessToken = "eyJhbGciOiJFUzM4NCJ9.eyJleHAiOjE3ODM1ODE1NDYsInN1YiI6ImNvcnJlY3QtdXNlcm5hbWUiLCJpYXQiOjE3ODM1ODA2NDZ9.HiHUnKNFruFXy-gWct-ZJc14FWX2ujEJ0wa2Y9SEuvIH2U8Esm_csB6hpFEJWIR9BlCQ6DHZCYgacj1ss-KeBWTck80ukpe0s6_D98lDsCV_T0595va5Wozyvf0c0W6Z";
        String expectedRefreshToken = "fNNgSWPl8LgCq7fCr5jGQkIr4uirrInp2tqizERn4JI=";

        doReturn(expectedAccessToken).when(accessTokenGenerator).generate(claims);
        doReturn(expectedRefreshToken).when(refreshTokenService).create(user);

        AuthResponse authResponse = tokenPairGenerator.create(user);

        assertThat(authResponse.accessToken()).isEqualTo(expectedAccessToken);
        assertThat(authResponse.refreshToken()).isEqualTo(expectedRefreshToken);
    }
}
