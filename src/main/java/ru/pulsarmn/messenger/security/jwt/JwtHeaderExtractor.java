package ru.pulsarmn.messenger.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class JwtHeaderExtractor {

    private static final String BEARER_PREFIX = "Bearer ";

    public Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return extractToken(authHeader);
    }

    public Optional<String> extractToken(StompHeaderAccessor headerAccessor) {
        String authHeader = headerAccessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        return extractToken(authHeader);
    }

    private Optional<String> extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring(BEARER_PREFIX.length()));
    }
}
