package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.entity.RefreshToken;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.repository.RefreshTokenRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;


@Service
public class RefreshTokenService {

    private final Clock clock;
    private final HashService hashService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(Clock clock, HashService hashService, RefreshTokenRepository refreshTokenRepository) {
        this.clock = clock;
        this.hashService = hashService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken find(String rawRefreshToken) {
        String refreshTokenHash = convertToHash(rawRefreshToken);
        return refreshTokenRepository.findByTokenHash(refreshTokenHash)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

    private String convertToHash(String rawRefreshToken) {
        byte[] refreshTokenBytes = Base64.getUrlDecoder().decode(rawRefreshToken);
        byte[] hashBytes = hashService.hash(refreshTokenBytes);
        return HexFormat.of().formatHex(hashBytes);
    }

    public boolean isExpired(RefreshToken refreshToken) {
        Instant currentTime = Instant.now(clock);
        return refreshToken.getExpiresAt().isBefore(currentTime);
    }

    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
