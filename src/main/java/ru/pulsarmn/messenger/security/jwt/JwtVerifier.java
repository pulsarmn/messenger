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
import java.util.*;


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
            boolean isValid = token.verify(verifier);
            if (isValid && isNonExpired(token)) {
                return convertToResult(token);
            }
            return JwtVerificationResult.failure(JwtVerificationResult.JwtErrorReason.INVALID_SIGNATURE);
        } catch (ParseException e) {
            return JwtVerificationResult.failure(JwtVerificationResult.JwtErrorReason.MALFORMED);
        } catch (JOSEException e) {
            return JwtVerificationResult.failure(JwtVerificationResult.JwtErrorReason.INVALID_SIGNATURE);
        }
    }

    private JwtVerificationResult convertToResult(SignedJWT token) throws ParseException {
        JWTClaimsSet claims = token.getJWTClaimsSet();
        UUID userId = UUID.fromString(claims.getSubject());
        String username = (String) claims.getClaim("username");
        Set<String> roles = extractRoles(claims);
        return JwtVerificationResult.success(userId, username, roles);
    }

    private Set<String> extractRoles(JWTClaimsSet claims) throws ParseException {
        List<String> rawRoles = claims.getStringListClaim("roles");
        if (rawRoles != null) {
            return new HashSet<>(rawRoles);
        }
        return Set.of();
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
