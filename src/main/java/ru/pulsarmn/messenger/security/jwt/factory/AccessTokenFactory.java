package ru.pulsarmn.messenger.security.jwt.factory;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;
import ru.pulsarmn.messenger.security.jwt.JwtClaims;
import ru.pulsarmn.messenger.security.jwt.JwtSigner;


@Component
public class AccessTokenFactory {

    private final JwtSigner jwtSigner;

    public AccessTokenFactory(JwtSigner jwtSigner) {
        this.jwtSigner = jwtSigner;
    }

    public String createAccessToken(JwtClaims jwtClaims) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.ES384);
        JWTClaimsSet claims = convertToNimbusClaims(jwtClaims);

        SignedJWT unsignedJwt = new SignedJWT(header, claims);
        jwtSigner.sign(unsignedJwt);
        return unsignedJwt.serialize();
    }

    private JWTClaimsSet convertToNimbusClaims(JwtClaims jwtClaims) {
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
        jwtClaims.getClaims().forEach(claimsBuilder::claim);
        return claimsBuilder.build();
    }
}
