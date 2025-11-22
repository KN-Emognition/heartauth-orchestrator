package knemognition.heartauth.orchestrator.security.app.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DecryptJweHandlerTest {

    private DecryptJweHandler handler;
    private KeyPair defaultRecipient;

    @BeforeEach
    void init() throws Exception {
        PemMapper mapper = Mappers.getMapper(PemMapper.class);
        defaultRecipient = generateKeyPair();
        handler = new DecryptJweHandler(new ObjectMapper(), mapper, (ECPrivateKey) defaultRecipient.getPrivate());
    }

    @Test
    void shouldDecryptUsingProvidedKeys() throws Exception {
        KeyPair recipient = generateKeyPair();
        KeyPair sender = generateKeyPair();
        String jwe = buildJwe(recipient, sender, "value", "secret");

        DecryptJweCmd<Map<String, Object>> cmd = DecryptJweCmd.<Map<String, Object>>builder()
                .jwe(jwe)
                .senderPublicKey(KeyLoader.toPem(sender.getPublic(), "PUBLIC KEY"))
                .recipientPrivateKey(KeyLoader.toPem(recipient.getPrivate(), "PRIVATE KEY"))
                .targetType(new TypeReference<>() {
                })
                .build();

        Map<String, Object> result = handler.handle(cmd);

        assertThat(result.get("value")).isEqualTo("secret");
    }

    @Test
    void shouldUseDefaultPrivateKeyWhenNotProvided() throws Exception {
        KeyPair sender = generateKeyPair();
        String jwe = buildJwe(defaultRecipient, sender, "flag", true);

        DecryptJweCmd<Map<String, Object>> cmd = DecryptJweCmd.<Map<String, Object>>builder()
                .jwe(jwe)
                .senderPublicKey(KeyLoader.toPem(sender.getPublic(), "PUBLIC KEY"))
                .targetType(new TypeReference<>() {
                })
                .build();

        Map<String, Object> result = handler.handle(cmd);

        assertThat(result.get("flag")).isEqualTo(true);
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        return generator.generateKeyPair();
    }

    private String buildJwe(KeyPair recipient, KeyPair sender, String claimKey, Object claimValue) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim(claimKey, claimValue)
                .build();
        SignedJWT signedJWT = new SignedJWT(
                new com.nimbusds.jose.JWSHeader.Builder(JWSAlgorithm.ES256).build(),
                claimsSet);
        signedJWT.sign(new ECDSASigner((ECPrivateKey) sender.getPrivate()));

        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.ECDH_ES, EncryptionMethod.A256GCM)
                .contentType("JWT")
                .build();
        JWEObject jweObject = new JWEObject(header, new Payload(signedJWT));
        jweObject.encrypt(new ECDHEncrypter((ECPublicKey) recipient.getPublic()));
        return jweObject.serialize();
    }
}
