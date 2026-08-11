package ru.pulsarmn.messenger.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.pulsarmn.messenger.security.jwt.JwtAuthorizationFilter;
import ru.pulsarmn.messenger.security.jwt.JwtVerificationResult;
import ru.pulsarmn.messenger.security.jwt.JwtVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterTest {

    @Mock
    private JwtVerifier jwtVerifier;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void initMocks() {
        SecurityContextHolder.clearContext();

        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtVerifier);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void doFilterInternal_whenValidAccessToken_shouldAuthenticate() throws Exception {
        String accessToken = "valid.access.token";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        JwtVerificationResult verificationResult = JwtVerificationResult.success("916e6bfe-1cc2-46bc-acda-713b5aa56d2e", "valid.username");
        UserPrincipal principal = new UserPrincipal(UUID.fromString("916e6bfe-1cc2-46bc-acda-713b5aa56d2e"), "valid.username");

        doReturn(verificationResult).when(jwtVerifier).verify(accessToken);

        jwtAuthorizationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(principal).isEqualTo(authentication.getPrincipal());
        assertThat(filterChain.getRequest()).isNotNull();
    }

    @Test
    void doFilterInternal_whenNullAccessToken_shouldNotAuthenticate() throws Exception {
        jwtAuthorizationFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_whenBearerPrefixIsAbsent_shouldNotAuthenticate() throws Exception {
        String accessToken = "invalid.access.token";
        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);

        jwtAuthorizationFilter.doFilter(request, response, filterChain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_whenInvalidOrExpiredAccessToken_shouldNotAuthenticate() throws Exception {
        String accessToken = "invalid.access.token";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        JwtVerificationResult verificationResult = JwtVerificationResult.failed("Invalid jwt token");

        doReturn(verificationResult).when(jwtVerifier).verify(accessToken);

        jwtAuthorizationFilter.doFilter(request, response, filterChain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
