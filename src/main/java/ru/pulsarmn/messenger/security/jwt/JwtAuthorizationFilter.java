package ru.pulsarmn.messenger.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pulsarmn.messenger.security.UserPrincipal;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtVerifier jwtVerifier;

    private static final String PREFIX = "Bearer";

    public JwtAuthorizationFilter(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith(PREFIX)) {
            String rawAccessToken = authorizationHeaderValue.substring(7);
            JwtVerificationResult verificationResult = jwtVerifier.verify(rawAccessToken);
            if (verificationResult.isValid()) {
                Authentication authentication = buildAuthentication(request, verificationResult);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication buildAuthentication(HttpServletRequest request, JwtVerificationResult verificationResult) {
        UserPrincipal userPrincipal = new UserPrincipal(UUID.fromString(verificationResult.getUserId()), verificationResult.getUsername());
        Set<SimpleGrantedAuthority> authorities = verificationResult.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
