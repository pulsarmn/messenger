package ru.pulsarmn.messenger.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.jwt.factory.TokenPairFactory;
import ru.pulsarmn.messenger.dto.request.AuthenticationRequest;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.dto.response.TokenPairResponse;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.exception.PasswordMismatchException;
import ru.pulsarmn.messenger.exception.UserAlreadyExistsException;
import ru.pulsarmn.messenger.exception.UserNotFoundException;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.UserRepository;


@Service
public class AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenPairFactory tokenPairFactory;

    public AuthService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenPairFactory tokenPairFactory) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenPairFactory = tokenPairFactory;
    }

    @Transactional
    public TokenPairResponse register(RegistrationRequest request) {
        checkUserExistence(request);
        validatePasswordsMatch(request);

        User user = mapToUser(request);
        user = userRepository.saveAndFlush(user);

        return tokenPairFactory.createTokenPair(user);
    }

    private void checkUserExistence(RegistrationRequest request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username '%s' already exists".formatted(username));
        }
    }

    private void validatePasswordsMatch(RegistrationRequest request) {
        if (!passwordsMatch(request)) {
            throw new BadCredentialsException("The passwords do not match");
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
    public TokenPairResponse authenticate(AuthenticationRequest request) {
        String username = request.username();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '%s' was not found".formatted(username)));

        String rawPassword = request.password();
        String encodedPassword = user.getPasswordHash();
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        return tokenPairFactory.createTokenPair(user);
    }
}
