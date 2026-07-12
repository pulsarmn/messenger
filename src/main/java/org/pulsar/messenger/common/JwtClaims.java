package org.pulsar.messenger.common;

import java.util.Map;


public interface JwtClaims {

    Object getClaim(String name);

    Map<String, Object> getAsMap();

    static JwtClaims of() {
        return new DefaultJwtClaims();
    }

    static JwtClaims of(Map<String, Object> claims) {
        return new DefaultJwtClaims(claims);
    }

    static DefaultJwtClaims.Builder builder() {
        return DefaultJwtClaims.builder();
    }
}
