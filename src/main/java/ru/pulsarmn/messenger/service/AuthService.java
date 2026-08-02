package ru.pulsarmn.messenger.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsarmn.messenger.jwt.AccessTokenFactory;
import ru.pulsarmn.messenger.jwt.JwtClaims;
import ru.pulsarmn.messenger.dto.AuthenticationRequest;
import ru.pulsarmn.messenger.dto.RegistrationRequest;
import ru.pulsarmn.messenger.dto.TokenPairResponse;
import ru.pulsarmn.messenger.entity.RefreshToken;
import ru.pulsarmn.messenger.entity.User;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.exception.PasswordMismatchException;
import ru.pulsarmn.messenger.exception.UserAlreadyExistsException;
import ru.pulsarmn.messenger.exception.UserNotFoundException;
import ru.pulsarmn.messenger.mapper.UserMapper;
import ru.pulsarmn.messenger.repository.RefreshTokenRepository;
import ru.pulsarmn.messenger.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;


@Service
public class AuthService {

    private final Clock clock;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenFactory accessTokenFactory;

    public AuthService(Clock clock, UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, AccessTokenFactory accessTokenFactory) {
        this.clock = clock;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenFactory = accessTokenFactory;
    }

    @Transactional
    public void register(RegistrationRequest request) {
        checkUserExistence(request);
        validatePasswordsMatch(request);

        User user = mapToUser(request);
        userRepository.saveAndFlush(user);
    }

    private void checkUserExistence(RegistrationRequest request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username '%s' already exists".formatted(username));
        }
    }

    private void validatePasswordsMatch(RegistrationRequest request) {
        if (!passwordsMatch(request)) {
            throw new BadCredentialsException("The passwords don't match");
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
            throw new PasswordMismatchException("Passwords do not matches");
        }

        return createResponse(user);
    }

    private TokenPairResponse createResponse(User user) {
        JwtClaims jwtClaims = buildClaims(user);
        String accessToken = accessTokenFactory.createAccessToken(jwtClaims);
        String refreshToken = generateRefreshToken(user);
        return new TokenPairResponse(accessToken, refreshToken);
    }

    private JwtClaims buildClaims(User user) {
        return JwtClaims.builder()
                .subject(user.getUsername())
                .expirationTime(getExpirationTime())
                .issueTime(getIssueTime())
                .build();
    }

    public String generateRefreshToken(User user) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] rawRefreshTokenBytes = new byte[32];
        secureRandom.nextBytes(rawRefreshTokenBytes);
        byte[] hashedRefreshTokenBytes = hash(rawRefreshTokenBytes);
        String hashedRefreshToken = HexFormat.of().formatHex(hashedRefreshTokenBytes);
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashedRefreshToken)
                .user(user)
                .expiresAt(getRefreshTokenExpirationTime())
                .build();
        refreshTokenRepository.saveAndFlush(refreshToken);
        return Base64.getUrlEncoder().encodeToString(rawRefreshTokenBytes);
    }

    private byte[] hash(byte[] rawBytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(rawBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Critical error: '%s' algorithm is missing".formatted("SHA-256"), e);
        }
    }

    private Instant getRefreshTokenExpirationTime() {
        return Instant.now(clock).plus(30, ChronoUnit.DAYS);
    }

    private Instant getExpirationTime() {
        return Instant.now(clock).plus(10, ChronoUnit.MINUTES);
    }

    private Instant getIssueTime() {
        return Instant.now(clock);
    }
}
