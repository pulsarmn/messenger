package org.pulsar.messenger.config;


import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.config.properties.JwtConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtConfigurationProperties.class)
public class JwtConfiguration {

    private final JwtConfigurationProperties jwtProperties;

    private static final String CRYPTO_ALGORITHM = "EC";

    @Bean
    ECPrivateKey accessTokenPrivateKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_ALGORITHM);
        KeySpec privateKeySpec = getPrivateKeySpec();
        return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }

    private KeySpec getPrivateKeySpec() {
        String rawPrivateKey = jwtProperties.privateKey();
        byte[] privateKeyBytes = Base64.getDecoder().decode(rawPrivateKey);
        return new PKCS8EncodedKeySpec(privateKeyBytes);
    }

    @Bean
    ECPublicKey accessTokenPublicKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_ALGORITHM);
        KeySpec publicKeySpec = getPublicKeySpec();
        return (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    private KeySpec getPublicKeySpec() {
        String rawPublicKey = jwtProperties.publicKey();
        byte[] publicKeyBytes = Base64.getDecoder().decode(rawPublicKey);
        return new X509EncodedKeySpec(publicKeyBytes);
    }
}
