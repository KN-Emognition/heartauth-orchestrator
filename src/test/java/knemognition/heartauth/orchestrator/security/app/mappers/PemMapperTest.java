package knemognition.heartauth.orchestrator.security.app.mappers;

import knemognition.heartauth.orchestrator.security.api.PemParsingException;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PemMapperTest {

    private PemMapper mapper;
    private KeyPair keyPair;

    @BeforeEach
    void init() throws Exception {
        mapper = Mappers.getMapper(PemMapper.class);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        keyPair = generator.generateKeyPair();
    }

    @Test
    void shouldParsePublicKey() throws Exception {
        var pem = KeyLoader.toPem(keyPair.getPublic(), "PUBLIC KEY");
        assertThat(mapper.publicMapAndValidate(pem)).isNotNull();
    }

    @Test
    void shouldParsePrivateKey() throws Exception {
        var pem = KeyLoader.toPem(keyPair.getPrivate(), "PRIVATE KEY");
        assertThat(mapper.privateMapAndValidate(pem)).isNotNull();
    }

    @Test
    void shouldRejectInvalidPem() {
        assertThatThrownBy(() -> mapper.publicMapAndValidate("bogus"))
                .isInstanceOf(PemParsingException.class);
    }
}
