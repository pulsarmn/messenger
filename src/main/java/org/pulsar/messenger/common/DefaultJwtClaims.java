package org.pulsar.messenger.common;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class DefaultJwtClaims implements JwtClaims {

    private final Map<String, Object> value;

    public DefaultJwtClaims() {
        this(new HashMap<>());
    }

    public DefaultJwtClaims(Map<String, Object> value) {
        this.value = value;
    }

    @Override
    public Object getClaim(String name) {
        return value.get(name);
    }

    @Override
    public Map<String, Object> getAsMap() {
        return new HashMap<>(value);
    }

    public static class Builder {

        private final Map<String, Object> claims = new HashMap<>();

        public Builder subject(String sub) {
            claims.put("sub", sub);
            return this;
        }

        public Builder expirationTime(Instant time) {
            claims.put("exp", time.getEpochSecond());
            return this;
        }

        public Builder issueTime(Instant time) {
            claims.put("iat", time.getEpochSecond());
            return this;
        }

        public Builder claim(String name, Object value) {
            claims.put(name, value);
            return this;
        }

        public DefaultJwtClaims build() {
            return new DefaultJwtClaims(claims);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
