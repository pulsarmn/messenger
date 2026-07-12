package org.pulsar.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.messenger.common.AccessTokenFactory;
import org.pulsar.messenger.common.DefaultJwtClaims;
import org.pulsar.messenger.common.JwtClaims;
import org.pulsar.messenger.common.RefreshTokenFactory;
import org.pulsar.messenger.dto.response.TokenResponse;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.repository.RefreshTokenRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TokenPairGeneratorTest {

    @Spy
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-07T10:00:00.00Z"), ZoneOffset.UTC);

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private AccessTokenFactory accessTokenFactory;

    @Mock
    private RefreshTokenFactory refreshTokenFactory;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private TokenPairGenerator tokenPairGenerator;

    private static final int DEFAULT_REFRESH_TOKEN_LENGTH = 32;

    @Test
    void create_whenValidUser_shouldReturnPairOfTokens() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("correct-username")
                .displayName("correct-display-name")
                .passwordHash("correct-password-hash")
                .build();
        byte[] refreshTokenBytes = {14, 5, -13, 54, -83, -91, -43, -118, 104, 9, 17, 7, -3, 6, 90, 69, -1, 56, -115, -35, 78, 17, -80, 54, 20, 126, 45, 63, 44, 51, -98, 93};
        String expectedAccessToken = "eyJhbGciOiJFUzM4NCJ9.eyJleHAiOjE3ODM1ODE1NDYsInN1YiI6ImNvcnJlY3QtdXNlcm5hbWUiLCJpYXQiOjE3ODM1ODA2NDZ9.HiHUnKNFruFXy-gWct-ZJc14FWX2ujEJ0wa2Y9SEuvIH2U8Esm_csB6hpFEJWIR9BlCQ6DHZCYgacj1ss-KeBWTck80ukpe0s6_D98lDsCV_T0595va5Wozyvf0c0W6Z";
        String expectedRefreshToken = "DgXzNq2l1YpoCREH_QZaRf84jd1OEbA2FH4tPywznl0=";

        doReturn(expectedAccessToken).when(accessTokenFactory).createAccessToken(Mockito.any(JwtClaims.class));
        doReturn(refreshTokenBytes).when(tokenGenerator).generate(DEFAULT_REFRESH_TOKEN_LENGTH);
        doReturn(RefreshToken.builder().build()).when(refreshTokenFactory).buildRefreshToken(refreshTokenBytes, user);

        TokenResponse tokenResponse = tokenPairGenerator.createResponse(user);

        assertThat(tokenResponse.accessToken()).isEqualTo(expectedAccessToken);
        assertThat(tokenResponse.refreshToken()).isEqualTo(expectedRefreshToken);
        verify(tokenGenerator).generate(DEFAULT_REFRESH_TOKEN_LENGTH);
        verify(refreshTokenFactory).buildRefreshToken(Mockito.any(byte[].class), Mockito.eq(user));
        verify(refreshTokenRepository).saveAndFlush(Mockito.any(RefreshToken.class));
    }
}
