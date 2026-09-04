package ru.pulsarmn.messenger.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsarmn.messenger.dto.request.AuthenticationRequest;
import ru.pulsarmn.messenger.dto.request.RefreshTokenRequest;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.dto.response.TokenPairResponse;
import ru.pulsarmn.messenger.service.AuthService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<TokenPairResponse> register(@Validated @RequestBody RegistrationRequest registrationRequest) {
        TokenPairResponse response = authService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    ResponseEntity<TokenPairResponse> login(@Validated @RequestBody AuthenticationRequest authenticationRequest) {
        TokenPairResponse response = authService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    ResponseEntity<TokenPairResponse> refresh(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenPairResponse response = authService.refresh(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }
}
