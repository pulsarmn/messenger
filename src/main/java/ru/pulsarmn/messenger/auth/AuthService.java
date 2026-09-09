package ru.pulsarmn.messenger.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.infrastructure.security.jwt.factory.TokenPairFactory;
import ru.pulsarmn.messenger.user.api.UserApi;
import ru.pulsarmn.messenger.user.api.dto.request.UserCreateRequest;
import ru.pulsarmn.messenger.user.api.dto.response.UserResponse;


@Service
public class AuthService {

    private final UserApi userApi;
    private final AuthUserMapper authUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenPairFactory tokenPairFactory;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserApi userApi, AuthUserMapper authUserMapper, PasswordEncoder passwordEncoder, TokenPairFactory tokenPairFactory, RefreshTokenService refreshTokenService) {
        this.userApi = userApi;
        this.authUserMapper = authUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenPairFactory = tokenPairFactory;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public TokenPairResponse register(RegistrationRequest request) {
        checkUserExistence(request);
        validatePasswordsMatch(request);

        UserCreateRequest userCreateRequest = mapToUser(request);
        UserResponse user = userApi.createUser(userCreateRequest);

        return tokenPairFactory.createTokenPair(user);
    }

    private void checkUserExistence(RegistrationRequest request) {
        userApi.findUserByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    }

    private void validatePasswordsMatch(RegistrationRequest request) {
        if (!passwordsMatch(request)) {
            throw new BadCredentialsException("The passwords do not match");
        }
    }

    private boolean passwordsMatch(RegistrationRequest request) {
        return (request.password()).equals(request.passwordConfirmation());
    }

    private UserCreateRequest mapToUser(RegistrationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        return authUserMapper.mapToCreateRequest(request, encodedPassword);
    }

    @Transactional
    public TokenPairResponse authenticate(AuthenticationRequest request) {
        String username = request.username();
        UserResponse user = userApi.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String rawPassword = request.password();
        String encodedPassword = user.passwordHash();
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        return tokenPairFactory.createTokenPair(user);
    }

    @Transactional
    public TokenPairResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.find(request.oldRefreshToken());
        checkRefreshTokenExpiration(refreshToken);

        refreshTokenService.delete(refreshToken);
        return userApi.findUserById(refreshToken.getUserId())
                .map(tokenPairFactory::createTokenPair)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

    private void checkRefreshTokenExpiration(RefreshToken refreshToken) {
        if (refreshTokenService.isExpired(refreshToken)) {
            refreshTokenService.delete(refreshToken);
            throw new BadCredentialsException("Refresh token has expired");
        }
    }
}
