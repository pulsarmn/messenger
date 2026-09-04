package ru.pulsarmn.messenger.security.jwt;

import java.util.Set;
import java.util.UUID;


public sealed interface JwtVerificationResult {

    boolean isValid();

    default boolean isInvalid() {
        return !isValid();
    }

    record Success(UUID userId, String username, Set<String> roles) implements JwtVerificationResult {

        @Override
        public boolean isValid() {
            return true;
        }
    }

    record Failure(JwtErrorReason errorReason) implements JwtVerificationResult {

        @Override
        public boolean isValid() {
            return false;
        }
    }

    enum JwtErrorReason {
        EXPIRED,
        MALFORMED,
        INVALID_SIGNATURE
    }

    static JwtVerificationResult success(UUID userId, String username, Set<String> roles) {
        return new Success(userId, username, roles);
    }

    static JwtVerificationResult failure(JwtErrorReason errorReason) {
        return new Failure(errorReason);
    }
}
