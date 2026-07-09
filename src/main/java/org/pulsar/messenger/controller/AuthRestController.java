package org.pulsar.messenger.controller;


import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.AuthRequest;
import org.pulsar.messenger.dto.TokenResponse;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Validated @RequestBody RegistrationRequest registrationRequest) {
        TokenResponse tokenResponse = authService.register(registrationRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Validated @RequestBody AuthRequest authRequest) {
        TokenResponse tokenResponse = authService.authenticate(authRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
