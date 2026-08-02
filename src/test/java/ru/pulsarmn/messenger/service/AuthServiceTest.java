package ru.pulsarmn.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.exception.UserAlreadyExistsException;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.UserRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_whenCorrectRequest_shouldRegisterNewUser() {
        RegistrationRequest request = new RegistrationRequest("correct_username", "correct_password", "correct_password");
        String encodedPassword = "correct_encoded_password";
        User.Builder builder = User.builder()
                .username(request.username())
                .passwordHash(encodedPassword);
        User mappedExpectedUser = builder.build();
        User savedExpectedUser = builder.id(UUID.randomUUID()).build();

        doReturn(false).when(userRepository).existsByUsername(request.username());
        doReturn(encodedPassword).when(passwordEncoder).encode(request.password());
        doReturn(mappedExpectedUser).when(userMapper).mapToEntity(request, encodedPassword);
        doReturn(savedExpectedUser).when(userRepository).saveAndFlush(mappedExpectedUser);

        authService.register(request);

        verify(userRepository, times(1)).existsByUsername(request.username());
        verify(passwordEncoder, times(1)).encode(request.password());
        verify(userMapper, times(1)).mapToEntity(request, encodedPassword);
        verify(userRepository, times(1)).saveAndFlush(mappedExpectedUser);
    }

    @Test
    void register_whenUsernameAlreadyTaken_shouldThrowUserAlreadyExistsException() {
        RegistrationRequest request = new RegistrationRequest("already_taken_username", "correct_password", "correct_password");

        doReturn(true).when(userRepository).existsByUsername(request.username());

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with username '%s' already exists".formatted(request.username()));
    }

    @Test
    void register_whenPasswordsDoNotMatch_shouldThrowBadCredentialsException() {
        RegistrationRequest request = new RegistrationRequest("correct_username", "correct_password", "incorrect_password");

        doReturn(false).when(userRepository).existsByUsername(request.username());

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("The passwords don't match");
    }
}
