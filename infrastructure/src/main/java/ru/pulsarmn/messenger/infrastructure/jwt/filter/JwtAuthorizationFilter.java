package ru.pulsarmn.messenger.infrastructure.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pulsarmn.messenger.infrastructure.jwt.JwtAuthenticationConverter;
import ru.pulsarmn.messenger.infrastructure.jwt.JwtHeaderExtractor;
import ru.pulsarmn.messenger.infrastructure.jwt.JwtVerificationResult;
import ru.pulsarmn.messenger.infrastructure.jwt.JwtVerifier;

import java.io.IOException;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtVerifier jwtVerifier;
    private final JwtHeaderExtractor headerExtractor;
    private final JwtAuthenticationConverter authenticationConverter;

    public JwtAuthorizationFilter(JwtVerifier jwtVerifier, JwtHeaderExtractor headerExtractor, JwtAuthenticationConverter authenticationConverter) {
        this.jwtVerifier = jwtVerifier;
        this.headerExtractor = headerExtractor;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        headerExtractor.extractToken(request)
                .map(jwtVerifier::verify)
                .filter(JwtVerificationResult::isValid)
                .map(vr -> authenticationConverter.toAuthentication(request, vr))
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }
}
