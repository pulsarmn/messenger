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
            case JwtVerificationResult.Success success -> {
                UsernamePasswordAuthenticationToken authentication = createAuthentication(success);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                yield authentication;
            }
            case JwtVerificationResult.Failure _ -> null;
        };
    }

    public Authentication toAuthentication(JwtVerificationResult verificationResult) {
        return switch (verificationResult) {
            case JwtVerificationResult.Success success -> createAuthentication(success);
            case JwtVerificationResult.Failure _ -> null;
        };
    }

    private UsernamePasswordAuthenticationToken createAuthentication(JwtVerificationResult.Success verificationResult) {
        UserPrincipal userPrincipal = UserPrincipal.of(verificationResult.userId(), verificationResult.username(), verificationResult.roles());
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }
}
