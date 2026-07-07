package org.pulsar.messenger.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.AuthResponse;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.mapper.UserMapper;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenPairGenerator tokenPairGenerator;

    @Transactional
    public AuthResponse register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.username())) {
            throw new UserAlreadyExistsException("User with username '%s' already exists".formatted(registrationRequest.username()));
        }

        validatePasswordsMatch(registrationRequest);
        User user = mapToUser(registrationRequest);

        User savedUser = userRepository.saveAndFlush(user);
        return tokenPairGenerator.create(savedUser);
    }

    private void validatePasswordsMatch(RegistrationRequest registrationRequest) {
        String password = registrationRequest.password();
        String passwordConfirmation = registrationRequest.passwordConfirmation();
        if (password == null || !password.equals(passwordConfirmation)) {
            throw new PasswordsMismatchException();
        }
    }

    private User mapToUser(RegistrationRequest registrationRequest) {
        String encodedPassword = passwordEncoder.encode(registrationRequest.password());
        return userMapper.mapToUser(registrationRequest, encodedPassword);
    }
}
