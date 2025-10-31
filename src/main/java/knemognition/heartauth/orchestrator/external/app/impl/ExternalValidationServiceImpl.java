package knemognition.heartauth.orchestrator.external.app.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityApi;
import knemognition.heartauth.orchestrator.security.app.exception.NonceValidationException;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.text.ParseException;
import java.util.Base64;

/**
 *  {@inheritDoc}
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalValidationServiceImpl implements SecurityApi {

    private final PemMapper pemMapper;

    /**
     *  {@inheritDoc}
     */
    @Override
    public void validateNonce(ValidateNonceCmd cmd) {
        try {
            Signature verifier = Signature.getInstance("SHA256withECDSAinP1363Format");
            verifier.initVerify(cmd.getPub());
            verifier.update(cmd.getNonce()
                    .getBytes(StandardCharsets.UTF_8));

            byte[] sig = Base64.getUrlDecoder()
                    .decode(cmd.getSignature());
            if (sig.length != 64) {
                throw new NonceValidationException("ES256 signature must be 64 bytes (R||S).");
            }
            if (!verifier.verify(sig)) {
                throw new NonceValidationException("Invalid signature.");
            }
        } catch (Exception e) {
            throw new NonceValidationException("Failed to verify ES256 signature");
        }
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public JWTClaimsSet decryptJwe(DecryptJweCmd cmd) throws JOSEException, ParseException {

        JWEObject jwe = JWEObject.parse(cmd.getJwe());
        JWEDecrypter decrypter = new ECDHDecrypter(cmd.getRecipientPrivateKey());
        jwe.decrypt(decrypter);

        SignedJWT signed = SignedJWT.parse(jwe.getPayload()
                .toString());

        if (!JWSAlgorithm.ES256.equals(signed.getHeader()
                .getAlgorithm())) {
            throw new JOSEException("Unexpected JWS alg: " + signed.getHeader()
                    .getAlgorithm());
        }

        if (!signed.verify(new ECDSAVerifier(cmd.getSenderPublicKey()))) {
            throw new JOSEException("Invalid signature");
        }

        return signed.getJWTClaimsSet();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void validatePublicKeyPem(String pem) {
        pemMapper.publicMapAndValidate(pem);
    }
}