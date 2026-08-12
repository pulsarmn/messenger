package ru.pulsarmn.messenger.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.security.UserPrincipal;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationConverter {

    public Authentication toAuthentication(HttpServletRequest request, JwtVerificationResult verificationResult) {
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
