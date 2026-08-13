package ru.pulsarmn.messenger.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
