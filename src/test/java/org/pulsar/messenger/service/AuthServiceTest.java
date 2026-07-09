package org.pulsar.messenger.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.messenger.dto.AuthRequest;
import org.pulsar.messenger.dto.AuthResponse;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.exception.UserNotFoundException;
import org.pulsar.messenger.mapper.UserMapper;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Spy
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenPairGenerator tokenPairGenerator;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_whenValidRegistrationRequest_shouldRegisterNewUserAndReturnPairOfTokens() {
        RegistrationRequest request = new RegistrationRequest("correct-username", "correct-password", "correct-password");
        User user = User.builder()
                .username(request.username())
                .displayName(request.username())
                .passwordHash("encoded-password")
                .build();
        AuthResponse expectedResponse = new AuthResponse("access-token", "refresh-token");

        doReturn(false).when(userRepository).existsByUsername(request.username());
        doReturn(user.getPasswordHash()).when(passwordEncoder).encode(request.password());
        doReturn(user).when(userMapper).mapToUser(request, user.getPasswordHash());
        doReturn(user).when(userRepository).saveAndFlush(user);
        doReturn(expectedResponse).when(tokenPairGenerator).create(user);

        AuthResponse actualResponse = authService.register(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void register_whenUserAlreadyExists_shouldThrowUserAlreadyExistsException() {
        RegistrationRequest request = new RegistrationRequest("existing-username", "correct-password", "correct-password");

        doReturn(true).when(userRepository).existsByUsername("existing-username");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with username '%s' already exists".formatted(request.username()));
    }

    @Test
    void register_whenPasswordsMismatch_shouldThrowPasswordsMismatchException() {
        RegistrationRequest request = new RegistrationRequest("correct-username", "one-password", "another-password");

        doReturn(false).when(userRepository).existsByUsername(request.username());

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(PasswordsMismatchException.class);
    }

    @Test
    void authenticate_whenValidAuthRequest_shouldAuthenticateAndReturnPairOfTokens() {
        AuthRequest authRequest = new AuthRequest("correct-username", "correct-password");
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(authRequest.username())
                .displayName(authRequest.username())
                .passwordHash("correct-password-hash")
                .build();
        AuthResponse expectedResponse = new AuthResponse("access-token", "refresh-token");

        doReturn(Optional.of(user)).when(userRepository).findByUsername(authRequest.username());
        doReturn(true).when(passwordEncoder).matches(authRequest.password(), user.getPasswordHash());
        doReturn(expectedResponse).when(tokenPairGenerator).create(user);

        AuthResponse actualResponse = authService.authenticate(authRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(userRepository, times(1)).findByUsername(authRequest.username());
        verify(passwordEncoder, times(1)).matches(authRequest.password(), user.getPasswordHash());
        verify(tokenPairGenerator, times(1)).create(user);
    }

    @Test
    void authenticate_whenUserNotExists_shouldThrowUserNotFoundException() {
        AuthRequest authRequest = new AuthRequest("non-existing-username", "some-password");

        doReturn(Optional.empty()).when(userRepository).findByUsername(authRequest.username());

        assertThatThrownBy(() -> authService.authenticate(authRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with username '%s' not found".formatted(authRequest.username()));
    }

    @Test
    void authenticate_whenInvalidPassword_shouldThrowPasswordMismatchException() {
        AuthRequest authRequest = new AuthRequest("correct-username", "invalid-password");
        User user = User.builder()
                .username(authRequest.username())
                .displayName(authRequest.username())
                .passwordHash("correct-password-hash")
                .build();

        doReturn(Optional.of(user)).when(userRepository).findByUsername(authRequest.username());
        doReturn(false).when(passwordEncoder).matches(authRequest.password(), user.getPasswordHash());

        assertThatThrownBy(() -> authService.authenticate(authRequest))
                .isInstanceOf(PasswordsMismatchException.class)
                .hasMessage("Invalid password");
    }
}
