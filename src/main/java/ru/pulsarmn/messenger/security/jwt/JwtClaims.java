package ru.pulsarmn.messenger.security.jwt;

import com.nimbusds.jwt.JWTClaimNames;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtClaims {

    private final Map<String, Object> claims;

    private JwtClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    public Object getClaim(String name) {
        return claims.get(name);
    }

    public String getSubject() {
        Object value = claims.get(JWTClaimNames.SUBJECT);
        if (value == null || value instanceof String) {
            return (String) value;
        } else {
            throw new RuntimeException("Subject is not String object");
        }
    }

    public Date getExpirationTime() {
        return getDateClaim(JWTClaimNames.EXPIRATION_TIME);
    }

    public Date getIssuedAtTime() {
        return getDateClaim(JWTClaimNames.ISSUED_AT);
    }

    private Date getDateClaim(String name) {
        Object value = claims.get(name);
        if (value == null || value instanceof Date) {
            return (Date) value;
        } else {
            throw new RuntimeException("The %s is not a Date".formatted(name));
        }
    }

    public Map<String, Object> getClaims() {
        return Collections.unmodifiableMap(claims);
    }

    public static class Builder {
        private final Map<String, Object> claims = new HashMap<>();

        public Builder subject(String sub) {
            claims.put(JWTClaimNames.SUBJECT, sub);
            return this;
        }

        public Builder expirationTime(Instant exp) {
            claims.put(JWTClaimNames.EXPIRATION_TIME, Date.from(exp));
            return this;
        }

        public Builder issueTime(Instant iat) {
            claims.put(JWTClaimNames.ISSUED_AT, Date.from(iat));
            return this;
        }

        public Builder claim(String name, Object obj) {
            claims.put(name, obj);
            return this;
        }

        public JwtClaims build() {
            return new JwtClaims(claims);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
