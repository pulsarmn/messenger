package ru.pulsarmn.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pulsarmn.messenger.dto.request.AuthenticationRequest;
import ru.pulsarmn.messenger.dto.request.RegistrationRequest;
import ru.pulsarmn.messenger.dto.response.TokenPairResponse;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.exception.PasswordMismatchException;
import ru.pulsarmn.messenger.exception.UserAlreadyExistsException;
import ru.pulsarmn.messenger.exception.UserNotFoundException;
import ru.pulsarmn.messenger.jwt.factory.TokenPairFactory;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private TokenPairFactory tokenPairFactory;

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
        TokenPairResponse expectedResponse = new TokenPairResponse("correct_access_token", "correct_refresh_token");

        doReturn(false).when(userRepository).existsByUsername(request.username());
        doReturn(encodedPassword).when(passwordEncoder).encode(request.password());
        doReturn(mappedExpectedUser).when(userMapper).mapToEntity(request, encodedPassword);
        doReturn(savedExpectedUser).when(userRepository).saveAndFlush(mappedExpectedUser);
        doReturn(expectedResponse).when(tokenPairFactory).createTokenPair(savedExpectedUser);

        TokenPairResponse actualResponse = authService.register(request);

        assertThat(actualResponse.accessToken()).isNotBlank();
        assertThat(actualResponse.refreshToken()).isNotBlank();
        assertThat(actualResponse).isEqualTo(expectedResponse);
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
                .hasMessage("The passwords do not match");
    }

    @Test
    void authenticate_whenCorrectCredentials_shouldAuthenticateUser() {
        AuthenticationRequest request = new AuthenticationRequest("correct_username", "correct_password");
        User user = User.builder()
                .username("correct_username")
                .passwordHash("password_hash")
                .build();
        TokenPairResponse expectedResponse = new TokenPairResponse("correct_access_token", "correct_refresh_token");

        doReturn(Optional.of(user)).when(userRepository).findByUsername(request.username());
        doReturn(true).when(passwordEncoder).matches(request.password(), user.getPasswordHash());
        doReturn(expectedResponse).when(tokenPairFactory).createTokenPair(user);

        TokenPairResponse actualResponse = authService.authenticate(request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(userRepository, times(1)).findByUsername(request.username());
        verify(passwordEncoder, times(1)).matches(request.password(), user.getPasswordHash());
        verify(tokenPairFactory, times(1)).createTokenPair(user);
    }

    @Test
    void authenticate_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("non_existing_username", "correct_password");

        doReturn(Optional.empty()).when(userRepository).findByUsername(request.username());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with username '%s' was not found".formatted(request.username()));
    }

    @Test
    void authenticate_whenPasswordsDoNotMatch_shouldThrowBadCredentialsException() {
        AuthenticationRequest request = new AuthenticationRequest("correct_username", "invalid_password");
        User user = User.builder()
                .username("correct_username")
                .passwordHash("password_hash")
                .build();

        doReturn(Optional.of(user)).when(userRepository).findByUsername(request.username());
        doReturn(false).when(passwordEncoder).matches(request.password(), user.getPasswordHash());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessage("Passwords do not match");
        verify(userRepository, times(1)).findByUsername(request.username());
        verify(passwordEncoder, times(1)).matches(request.password(), user.getPasswordHash());
    }
}
