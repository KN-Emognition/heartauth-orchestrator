package knemognition.heartauth.orchestrator.shared.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.text.ParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EcgDataTokenDecryptor {

    public static JWTClaimsSet decryptAndVerify(String compactJwe, ECPrivateKey recipientPrivateKey, ECPublicKey senderPublicKey) throws JOSEException, ParseException {

        JWEObject jwe = JWEObject.parse(compactJwe);
        JWEDecrypter decrypter = new ECDHDecrypter(recipientPrivateKey);
        jwe.decrypt(decrypter);

        SignedJWT signed = SignedJWT.parse(jwe.getPayload().toString());

        if (!JWSAlgorithm.ES256.equals(signed.getHeader().getAlgorithm())) {
            throw new JOSEException("Unexpected JWS alg: " + signed.getHeader().getAlgorithm());
        }

        if (!signed.verify(new ECDSAVerifier(senderPublicKey))) {
            throw new JOSEException("Invalid signature");
        }

        return signed.getJWTClaimsSet();
    }
}
