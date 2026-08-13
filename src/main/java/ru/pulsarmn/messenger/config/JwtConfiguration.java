package ru.pulsarmn.messenger.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pulsarmn.messenger.config.properties.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

    private final JwtProperties jwtProperties;

    private static final String ALGORITHM = "EC";

    public JwtConfiguration(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    ECPrivateKey accessTokenPrivateKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        KeySpec privateKeySpec = getPrivateKeySpec();
        return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }

    private KeySpec getPrivateKeySpec() {
        String rawPrivateKey = jwtProperties.getPrivateKey();
        byte[] privateKeyBytes = Base64.getDecoder().decode(rawPrivateKey.getBytes(StandardCharsets.UTF_8));
        return new PKCS8EncodedKeySpec(privateKeyBytes);
    }

    @Bean
    ECPublicKey accessTokenPublicKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        KeySpec publicKeySpec = getPublicKeySpec();
        return (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    private KeySpec getPublicKeySpec() {
        String rawPublicKey = jwtProperties.getPublicKey();
        byte[] publicKeyBytes = Base64.getDecoder().decode(rawPublicKey.getBytes(StandardCharsets.UTF_8));
        return new X509EncodedKeySpec(publicKeyBytes);
    }
}
