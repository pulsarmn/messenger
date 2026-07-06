package org.pulsar.messenger.service;


import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;


@Service
public class HashService {

    private static final String HASH_ALGORITHM = "SHA-256";

    public String hash(byte[] rawBytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = messageDigest.digest(rawBytes);
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException _) {
            // shouldn't happen
            throw new RuntimeException();
        }
    }
}
