package org.pulsar.messenger.service;


import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;


@Service
public class HashService {

    private static final String HASH_ALGORITHM = "SHA-256";

    public String hash(byte[] rawBytes) {
        Objects.requireNonNull(rawBytes, "Bytes array must not be null");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = messageDigest.digest(rawBytes);
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // shouldn't happen
            throw new IllegalStateException("Critical error: %s algorithm is missing".formatted(HASH_ALGORITHM), e);
        }
    }
}
