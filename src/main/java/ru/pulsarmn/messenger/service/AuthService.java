package ru.pulsarmn.messenger.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.security.interfaces.ECPrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;


@Service
public class AuthService {

    private final Clock clock;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ECPrivateKey accessTokenPrivateKey;

    public AuthService(Clock clock, UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, ECPrivateKey accessTokenPrivateKey) {
        this.clock = clock;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenPrivateKey = accessTokenPrivateKey;
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

        String accessToken = generateAccessToken(username);
        String refreshToken = generateRefreshToken(user);
        return new TokenPairResponse(accessToken, refreshToken);
    }

    public String generateAccessToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.ES384);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(getExpirationTime())
                .issueTime(getIssueTime())
                .build();

        try {
            ECDSASigner signer = new ECDSASigner(accessTokenPrivateKey, Curve.P_384);
            SignedJWT signedJwt = new SignedJWT(header, claims);
            signedJwt.sign(signer);
            return signedJwt.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(); // TODO
        }
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

    private Date getExpirationTime() {
        return Date.from(Instant.now(clock).plus(10, ChronoUnit.MINUTES));
    }

    private Date getIssueTime() {
        return Date.from(Instant.now(clock));
    }
}
