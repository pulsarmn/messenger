package ru.pulsarmn.messenger.controller.interceptor;

import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.security.jwt.JwtAuthenticationConverter;
import ru.pulsarmn.messenger.security.jwt.JwtHeaderExtractor;
import ru.pulsarmn.messenger.security.jwt.JwtVerificationResult;
import ru.pulsarmn.messenger.security.jwt.JwtVerifier;

import java.util.Optional;


@Component
public class JwtAuthorizationChannelInterceptor implements ChannelInterceptor {

    private final JwtVerifier jwtVerifier;
    private final JwtHeaderExtractor jwtHeaderExtractor;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public JwtAuthorizationChannelInterceptor(JwtVerifier jwtVerifier, JwtHeaderExtractor jwtHeaderExtractor, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtVerifier = jwtVerifier;
        this.jwtHeaderExtractor = jwtHeaderExtractor;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor == null) {
            return null;
        }

        Optional<String> token = jwtHeaderExtractor.extractToken(headerAccessor);
        if (token.isEmpty()) {
            return null;
        }

        String rawAccessToken = token.get();
        JwtVerificationResult verificationResult = jwtVerifier.verify(rawAccessToken);
        if (verificationResult.isInvalid()) {
            return null;
        }

        Authentication authentication = jwtAuthenticationConverter.toAuthentication(verificationResult);
        if (authentication == null) {
            return null;
        }

        headerAccessor.setUser(authentication);
        return message;
    }
}
