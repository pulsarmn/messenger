package ru.pulsarmn.messenger.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class HashService {

    private static final String DEFAULT_HASH_ALGORITHM = "SHA-256";

    public byte[] hash(byte[] rawBytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DEFAULT_HASH_ALGORITHM);
            return messageDigest.digest(rawBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Critical error: '%s' algorithm is missing".formatted(DEFAULT_HASH_ALGORITHM), e);
        }
    }
}
