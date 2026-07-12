package org.pulsar.messenger.service;

import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.request.AuthRequest;
import org.pulsar.messenger.dto.request.RegistrationRequest;
import org.pulsar.messenger.dto.response.TokenResponse;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.exception.UserNotFoundException;
import org.pulsar.messenger.mapper.UserMapper;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenPairGenerator tokenPairGenerator;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse register(RegistrationRequest request) {
        Objects.requireNonNull(request);
        checkUserExistence(request);
        validatePasswordsMatch(request);

        User user = mapToUser(request);
        user = userRepository.saveAndFlush(user);

        return tokenPairGenerator.createResponse(user);
    }

    private void checkUserExistence(RegistrationRequest request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username '%s' already exists".formatted(username));
        }
    }

    private void validatePasswordsMatch(RegistrationRequest request) {
        if (!passwordsMatch(request)) {
            throw new PasswordsMismatchException();
        }
    }

    private boolean passwordsMatch(RegistrationRequest request) {
        return (request.password()).equals(request.passwordConfirmation());
    }

    private User mapToUser(RegistrationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        return userMapper.mapToEntity(request, encodedPassword);
    }

    @Transactional
    public TokenResponse authenticate(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.username())
                .orElseThrow(() -> new UserNotFoundException("User with username '%s' not found".formatted(authRequest.username())));
        validatePasswordsMatch(authRequest.password(), user.getPasswordHash());
        return tokenPairGenerator.createResponse(user);
    }

    private void validatePasswordsMatch(String rawPassword, String passwordHash) {
        if (!passwordEncoder.matches(rawPassword, passwordHash)) {
            throw new PasswordsMismatchException("Invalid password");
        }
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        User user = refreshTokenService.refresh(refreshToken);
        return tokenPairGenerator.createResponse(user);
    }
}
