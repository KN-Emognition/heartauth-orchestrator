package knemognition.heartauth.orchestrator.security.app.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import knemognition.heartauth.orchestrator.security.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@Service
public class DecryptJweHandler {
    private final ObjectMapper objectMapper;
    private final PemMapper pemMapper;
    private final ECPrivateKey privateKey;

    public <T> T handle(DecryptJweCmd<T> cmd) throws JOSEException, ParseException {
        var privateKey = cmd.getRecipientPrivateKey() == null
                ? this.privateKey
                : pemMapper.privateMapAndValidate(cmd.getRecipientPrivateKey());
        var publicKey = pemMapper.publicMapAndValidate(cmd.getSenderPublicKey());
        var jwe = JWEObject.parse(cmd.getJwe());
        var decrypter = new ECDHDecrypter(privateKey);
        jwe.decrypt(decrypter);

        var signed = SignedJWT.parse(jwe.getPayload()
                .toString());

        if (!JWSAlgorithm.ES256.equals(signed.getHeader()
                .getAlgorithm())) {
            throw new JOSEException("Unexpected JWS alg: " + signed.getHeader()
                    .getAlgorithm());
        }

        if (!signed.verify(new ECDSAVerifier(publicKey))) {
            throw new JOSEException("Invalid signature");
        }

        return objectMapper.convertValue(signed.getJWTClaimsSet()
                .getClaims(), cmd.getTargetType());
    }
}
