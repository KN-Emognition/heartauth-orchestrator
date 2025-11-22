package knemognition.heartauth.orchestrator.security.app.handlers;

import knemognition.heartauth.orchestrator.security.api.NonceValidationException;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidateNonceHandlerTest {

    private ValidateNonceHandler handler;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws Exception {
        PemMapper pemMapper = Mappers.getMapper(PemMapper.class);
        handler = new ValidateNonceHandler(pemMapper);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        keyPair = generator.generateKeyPair();
    }

    @Test
    void shouldValidateCorrectSignature() throws Exception {
        String nonce = "nonce-value";
        byte[] signatureBytes = sign(nonce);
        String signature = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
        ValidateNonceCmd cmd = ValidateNonceCmd.builder()
                .nonce(nonce)
                .signature(signature)
                .pub(KeyLoader.toPem(keyPair.getPublic(), "PUBLIC KEY"))
                .build();

        assertThatCode(() -> handler.handle(cmd)).doesNotThrowAnyException();
    }

    @Test
    void shouldFailForInvalidSignature() throws Exception {
        String nonce = "nonce-value";
        byte[] signatureBytes = sign("different");
        String signature = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
        ValidateNonceCmd cmd = ValidateNonceCmd.builder()
                .nonce(nonce)
                .signature(signature)
                .pub(KeyLoader.toPem(keyPair.getPublic(), "PUBLIC KEY"))
                .build();

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(NonceValidationException.class);
    }

    private byte[] sign(String nonce) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSAinP1363Format");
        signature.initSign(keyPair.getPrivate());
        signature.update(nonce.getBytes());
        return signature.sign();
    }
}
