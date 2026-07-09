package org.pulsar.messenger.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.messenger.dto.AuthResponse;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.mapper.UserMapper;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;


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
}
