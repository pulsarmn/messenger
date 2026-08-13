package ru.pulsarmn.messenger.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.security.UserPrincipal;


@Component
public class JwtAuthenticationConverter {

    public Authentication toAuthentication(HttpServletRequest request, JwtVerificationResult verificationResult) {
        return switch (verificationResult) {
            case JwtVerificationResult.Success success -> createAuthentication(success, request);
            case JwtVerificationResult.Failure _ -> null;
        };
    }

    private Authentication createAuthentication(JwtVerificationResult.Success verificationResult, HttpServletRequest request) {
        UserPrincipal userPrincipal = UserPrincipal.of(verificationResult.userId(), verificationResult.username(), verificationResult.roles());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
