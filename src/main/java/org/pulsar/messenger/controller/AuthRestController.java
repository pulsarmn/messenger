package org.pulsar.messenger.controller;


import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegistrationRequest registrationRequest) {
        String password = registrationRequest.password();
        String passwordConfirmation = registrationRequest.passwordConfirmation();

        if (passwordsMatch(password, passwordConfirmation)) {
            User user = mapToUser(registrationRequest);
            String encodedPassword = passwordEncoder.encode(password);
            user.setPasswordHash(encodedPassword);

            try {
                userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Dolbaeb");
            }
        } else {
            throw new PasswordsMismatchException();
        }
        return ResponseEntity.ok().build();
    }

    private User mapToUser(RegistrationRequest registrationRequest) {
        return User.builder()
                .username(registrationRequest.username())
                .displayName(registrationRequest.username())
                .build();
    }

    private boolean passwordsMatch(String sourcePassword, String passwordConfirmation) {
        return sourcePassword != null && sourcePassword.equals(passwordConfirmation);
    }
}
