package org.pulsar.messenger.service;


import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class AccessTokenGeneratorTest {

    private AccessTokenGenerator accessTokenGenerator;
    private ECPublicKey publicKey;

    @BeforeEach
    void setUp() throws Exception {
        KeyPair keyPair = createKeyPair();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();

        this.publicKey = (ECPublicKey) keyPair.getPublic();
        this.accessTokenGenerator = new AccessTokenGenerator(privateKey);
    }

    private KeyPair createKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(384);
        return keyPairGenerator.genKeyPair();
    }

    @Test
    void generate_whenPayloadIsValid_shouldReturnStrAccessToken() throws Exception {
        Map<String, Object> payload = Map.of(
                "sub", "pulsar",
                "custom-claim", "custom-value"
        );

        String strAccessToken = accessTokenGenerator.generate(payload);
        ECDSAVerifier jwsVerifier = new ECDSAVerifier(publicKey);
        SignedJWT accessToken = SignedJWT.parse(strAccessToken);

        assertThat(accessToken.verify(jwsVerifier)).isTrue();
        assertThat(accessToken.getJWTClaimsSet().getSubject()).isEqualTo("pulsar");
        assertThat(accessToken.getJWTClaimsSet().getClaim("custom-claim")).isEqualTo("custom-value");
    }
}
