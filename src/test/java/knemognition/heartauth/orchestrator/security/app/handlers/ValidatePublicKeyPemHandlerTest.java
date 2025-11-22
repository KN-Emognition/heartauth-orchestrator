package knemognition.heartauth.orchestrator.security.app.handlers;

import knemognition.heartauth.orchestrator.security.api.PemParsingException;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatePublicKeyPemHandlerTest {

    private ValidatePublicKeyPemHandler handler;
    private String validPem;

    @BeforeEach
    void init() throws Exception {
        PemMapper pemMapper = Mappers.getMapper(PemMapper.class);
        handler = new ValidatePublicKeyPemHandler(pemMapper);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair pair = generator.generateKeyPair();
        validPem = KeyLoader.toPem(pair.getPublic(), "PUBLIC KEY");
    }

    @Test
    void shouldAcceptValidPublicKey() {
        assertThatCode(() -> handler.handle(validPem)).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectInvalidPublicKey() {
        assertThatThrownBy(() -> handler.handle("invalid pem"))
                .isInstanceOf(PemParsingException.class);
    }
}
