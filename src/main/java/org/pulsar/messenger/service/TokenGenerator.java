package org.pulsar.messenger.service;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;


@Component
public class TokenGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    public byte[] generate(int tokenLength) {
        if (tokenLength <= 0) {
            return new byte[0];
        }

        byte[] tokenBytes = new byte[tokenLength];
        secureRandom.nextBytes(tokenBytes);
        return tokenBytes;
    }
}
