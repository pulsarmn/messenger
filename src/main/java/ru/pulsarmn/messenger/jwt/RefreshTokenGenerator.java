package ru.pulsarmn.messenger.jwt;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;


@Component
public class RefreshTokenGenerator {

    private final SecureRandom secureRandom;

    public RefreshTokenGenerator() {
        this(new SecureRandom());
    }

    public RefreshTokenGenerator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public byte[] generate(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be greater than 0");
        }

        byte[] tokenBytes = new byte[length];
        secureRandom.nextBytes(tokenBytes);
        return tokenBytes;
    }
}
