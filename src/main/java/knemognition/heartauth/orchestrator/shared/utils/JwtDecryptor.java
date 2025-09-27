package knemognition.heartauth.orchestrator.shared.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public final class JwtDecryptor {

    private JwtDecryptor() {
    }

    public static JWTClaimsSet decryptAndVerify(
            String compactJwe,
            ECPrivateKey recipientPrivateKey,
            ECPublicKey senderPublicKey
    ) throws JOSEException, java.text.ParseException {

        // 1) Decrypt the outer JWE
        JWEObject jwe = JWEObject.parse(compactJwe);
        JWEDecrypter decrypter = new ECDHDecrypter(recipientPrivateKey);
        jwe.decrypt(decrypter);

        // 2) Parse the inner JWS
        SignedJWT signed = SignedJWT.parse(jwe.getPayload().toString());

        // Enforce expected algorithm (ES256 usually for P-256)
        if (!JWSAlgorithm.ES256.equals(signed.getHeader().getAlgorithm())) {
            throw new JOSEException("Unexpected JWS alg: " + signed.getHeader().getAlgorithm());
        }

        // 3) Verify signature
        if (!signed.verify(new ECDSAVerifier(senderPublicKey))) {
            throw new JOSEException("Invalid signature");
        }

        // 4) Return claims (throws if payload isn’t valid JWT claims)
        return signed.getJWTClaimsSet();
    }
}
