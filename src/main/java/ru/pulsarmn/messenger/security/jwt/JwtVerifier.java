package ru.pulsarmn.messenger.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtVerifier {

    private final Clock clock;
    private final ECPublicKey accessTokenPublicKey;

    public JwtVerifier(Clock clock, ECPublicKey accessTokenPublicKey) {
        this.clock = clock;
        this.accessTokenPublicKey = accessTokenPublicKey;
    }

    public JwtVerificationResult verify(String rawAccessToken) {
        try {
            SignedJWT token = SignedJWT.parse(rawAccessToken);
            JWSVerifier verifier = new ECDSAVerifier(accessTokenPublicKey);
            if (token.verify(verifier) && isNonExpired(token)) {
                JWTClaimsSet claims = token.getJWTClaimsSet();
                String subject = claims.getSubject();
                Object username = claims.getClaim("username");
                if (username instanceof String strUsername) {
                    return JwtVerificationResult.success(subject, strUsername);
                }
            }
            return JwtVerificationResult.failed("Invalid or expired JWT token");
        } catch (JOSEException | ParseException e) {
            return JwtVerificationResult.failed("Invalid or expired JWT token");
        }
    }

    private boolean isNonExpired(SignedJWT token) throws ParseException {
        Date expirationTime = token.getJWTClaimsSet().getExpirationTime();
        if (expirationTime == null) {
            return false;
        }
        Instant currentTime = Instant.now(clock);
        return currentTime.isBefore(expirationTime.toInstant());
    }
}
