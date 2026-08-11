package ru.pulsarmn.messenger.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPrivateKey;


@Component
public class JwtSigner {

    private final ECPrivateKey accessTokenPrivateKey;

    public JwtSigner(ECPrivateKey accessTokenPrivateKey) {
        this.accessTokenPrivateKey = accessTokenPrivateKey;
    }

    public void sign(SignedJWT signedJWT) {
        try {
            JWSSigner signer = new ECDSASigner(accessTokenPrivateKey);
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
