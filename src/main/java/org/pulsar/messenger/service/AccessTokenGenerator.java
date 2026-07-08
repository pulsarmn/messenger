package org.pulsar.messenger.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.exception.TokenGenerationException;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPrivateKey;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class AccessTokenGenerator {

    private final ECPrivateKey accessTokenPrivateKey;

    public String generate(Map<String, Object> payload) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES384).build();
        JWTClaimsSet claims = convertToClaims(payload);

        try {
            JWSSigner jwsSigner = new ECDSASigner(accessTokenPrivateKey, Curve.P_384);
            SignedJWT token = new SignedJWT(header, claims);
            token.sign(jwsSigner);

            return token.serialize();
        } catch (JOSEException e) {
            throw new TokenGenerationException("Failed to sign access token", e);
        }
    }

    private JWTClaimsSet convertToClaims(Map<String, Object> payload) {
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
        if (payload != null) payload.forEach(claimsBuilder::claim);
        return claimsBuilder.build();
    }
}
