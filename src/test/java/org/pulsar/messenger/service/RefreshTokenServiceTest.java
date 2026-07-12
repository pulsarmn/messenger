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
import org.pulsar.messenger.exception.BadCredentialsException;
import org.pulsar.messenger.repository.RefreshTokenRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private static final String RAW_TOKEN = "I3594kEb5qLQnZbru6cq78ZF9M40vW55nfEmUajUOfw=";
    private static final byte[] RAW_TOKEN_BYTES = {35, 126, 125, -30, 65, 27, -26, -94, -48, -99, -106, -21, -69, -89, 42, -17, -58, 69, -12, -50, 52, -67, 110, 121, -99, -15, 38, 81, -88, -44, 57, -4};
    private static final String TOKEN_HASH = "462079d9c63786260300f75d5de5b103cab45d256c4be20091febeb47619e28c";

    @Test
    void find_whenExistingRefreshToken_shouldReturnRefreshToken() {
        RefreshToken expectedRefreshToken = RefreshToken.builder()
                .tokenHash(TOKEN_HASH)
                .build();

        doReturn(TOKEN_HASH).when(hashService).hash(RAW_TOKEN_BYTES);
        doReturn(Optional.of(expectedRefreshToken)).when(refreshTokenRepository).findByTokenHash(TOKEN_HASH);

        RefreshToken actualRefreshToken = refreshTokenService.find(RAW_TOKEN);

        assertThat(actualRefreshToken).isEqualTo(expectedRefreshToken);
        verify(hashService, times(1)).hash(RAW_TOKEN_BYTES);
        verify(refreshTokenRepository, times(1)).findByTokenHash(TOKEN_HASH);
    }

    @Test
    void find_whenNonExistingRefreshToken_shouldThrowException() {
        doReturn(TOKEN_HASH).when(hashService).hash(RAW_TOKEN_BYTES);
        doReturn(Optional.empty()).when(refreshTokenRepository).findByTokenHash(TOKEN_HASH);

        assertThatThrownBy(() -> refreshTokenService.find(RAW_TOKEN))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid refresh token");
        verify(hashService, times(1)).hash(RAW_TOKEN_BYTES);
        verify(refreshTokenRepository, times(1)).findByTokenHash(TOKEN_HASH);
    }

    @Test
    void checkExpiration_whenNonExpiredToken_shouldDoNothing() {
        RefreshToken refreshToken = RefreshToken.builder()
                .expiresAt(Instant.now(clock).plus(15, ChronoUnit.DAYS))
                .build();

        refreshTokenService.checkExpiration(refreshToken);
    }

    @Test
    void checkExpiration_whenExpiredToken_shouldThrowException() {
        RefreshToken refreshToken = RefreshToken.builder()
                .expiresAt(Instant.now(clock).minus(15, ChronoUnit.DAYS))
                .build();

        assertThatThrownBy(() -> refreshTokenService.checkExpiration(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Refresh token has expired");
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }
}
