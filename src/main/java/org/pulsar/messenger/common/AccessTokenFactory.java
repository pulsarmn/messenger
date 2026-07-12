package org.pulsar.messenger.common;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.exception.TokenGenerationException;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPrivateKey;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class AccessTokenFactory {

    private final ECPrivateKey accessTokenPrivateKey;

    public String createAccessToken(JwtClaims jwtClaims) {
        SignedJWT token = doCreate(jwtClaims);
        signToken(token);
        return token.serialize();
    }

    private SignedJWT doCreate(JwtClaims jwtClaims) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.ES384);
        JWTClaimsSet claims = convertToNimbusClaims(jwtClaims);
        return new SignedJWT(header, claims);
    }

    private JWTClaimsSet convertToNimbusClaims(JwtClaims domainClaims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        domainClaims.getAsMap().forEach((key, value) -> {
            if (value instanceof Instant instant) {
                builder.claim(key, instant.getEpochSecond());
            } else {
                builder.claim(key, value);
            }
        });
        return builder.build();
    }

    private void signToken(SignedJWT token) {
        try {
            JWSSigner signer = new ECDSASigner(accessTokenPrivateKey);
            token.sign(signer);
        } catch (JOSEException e) {
            throw new TokenGenerationException(e);
        }
    }
}
