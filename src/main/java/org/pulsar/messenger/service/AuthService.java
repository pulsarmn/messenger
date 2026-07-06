package org.pulsar.messenger.service;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.pulsar.messenger.dto.AuthResponse;
import org.pulsar.messenger.dto.RegistrationRequest;
import org.pulsar.messenger.entity.User;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.mapper.UserMapper;
import org.pulsar.messenger.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.interfaces.ECPrivateKey;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ECPrivateKey accessTokenPrivateKey;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse register(RegistrationRequest registrationRequest) {
        checkPasswords(registrationRequest);
        User user = mapToUser(registrationRequest);

        try {
            User savedUser = userRepository.save(user);
            return createAuthResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with username '%s' already exists");
        }
    }

    private AuthResponse createAuthResponse(User user) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES384).build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder().build();

        String accessToken;
        try {
            JWSSigner jwsSigner = new ECDSASigner(accessTokenPrivateKey, Curve.P_384);
            SignedJWT token = new SignedJWT(header, claims);
            token.sign(jwsSigner);

            accessToken = token.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        String refreshToken = refreshTokenService.create(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    private void checkPasswords(RegistrationRequest registrationRequest) {
        String password = registrationRequest.password();
        String passwordConfirmation = registrationRequest.passwordConfirmation();
        if (password == null || !password.equals(passwordConfirmation)) {
            throw new PasswordsMismatchException();
        }
    }

    private User mapToUser(RegistrationRequest registrationRequest) {
        User user = userMapper.mapToUser(registrationRequest);
        String encodedPassword = passwordEncoder.encode(registrationRequest.password());
        user.setPasswordHash(encodedPassword);

        return user;
    }
}
