package ru.pulsarmn.messenger.security.jwt;

import java.util.*;


public class JwtVerificationResult {

    private final String userId;
    private final String username;
    private final boolean isValid;
    private final String error;
    private final Set<String> roles = new HashSet<>();

    private JwtVerificationResult(String userId, String username, boolean isValid, String error, Collection<String> roles) {
        this.userId = userId;
        this.username = username;
        this.isValid = isValid;
        this.error = error;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public static JwtVerificationResult success(String userId, String username) {
        return success(userId, username, List.of());
    }

    public static JwtVerificationResult success(String userId, String username, Collection<String> roles) {
        return new JwtVerificationResult(userId, username, true, null, roles);
    }

    public static JwtVerificationResult failed(String error) {
        return new JwtVerificationResult(null, null, false, error, List.of());
    }

    public boolean isValid() {
        return isValid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public String getError() {
        return error;
    }
}
