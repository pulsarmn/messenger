package org.pulsar.messenger.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.messenger.entity.RefreshToken;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.repository.RefreshTokenRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Spy
    private Clock clock = Clock.fixed(Instant.parse("2026-07-07T10:00:00.00Z"), ZoneOffset.UTC);

    @Mock
    private HashService hashService;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private static final int DEFAULT_TOKEN_LENGTH = 32;
    private static final byte[] RAW_TOKEN_BYTES = {35, 126, 125, -30, 65, 27, -26, -94, -48, -99, -106, -21, -69, -89, 42, -17, -58, 69, -12, -50, 52, -67, 110, 121, -99, -15, 38, 81, -88, -44, 57, -4};
    private static final String TOKEN_HASH = "462079d9c63786260300f75d5de5b103cab45d256c4be20091febeb47619e28c";

    @Test
    void create_whenCorrectUser_shouldCreateRefreshToken() {
        User user = User.builder()
                .username("correct_username")
                .displayName("correct_display_name")
                .passwordHash("correct_password_hash")
                .build();
        String expectedToken = "I3594kEb5qLQnZbru6cq78ZF9M40vW55nfEmUajUOfw=";

        doReturn(RAW_TOKEN_BYTES).when(tokenGenerator).generate(DEFAULT_TOKEN_LENGTH);
        doReturn(TOKEN_HASH).when(hashService).hash(RAW_TOKEN_BYTES);

        String actualRefreshToken = refreshTokenService.create(user);

        assertThat(actualRefreshToken).isEqualTo(expectedToken);
        verify(tokenGenerator, times(1)).generate(DEFAULT_TOKEN_LENGTH);
        verify(hashService, times(1)).hash(RAW_TOKEN_BYTES);
        verify(refreshTokenRepository, times(1)).saveAndFlush(Mockito.any(RefreshToken.class));
    }

    @Test
    void create_whenUserIsNull_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> refreshTokenService.create(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User must not be null for refresh token creation");

        verifyNoInteractions(tokenGenerator, hashService, refreshTokenRepository);
    }
}
