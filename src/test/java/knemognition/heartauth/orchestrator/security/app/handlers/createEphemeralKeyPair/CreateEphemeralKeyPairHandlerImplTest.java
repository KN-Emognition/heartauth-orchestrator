package knemognition.heartauth.orchestrator.security.app.handlers.createEphemeralKeyPair;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;

class CreateEphemeralKeyPairHandlerImplTest {

    @Test
    void shouldGenerateEcKeyPair() {
        CreateEphemeralKeyPairHandlerImpl handler = new CreateEphemeralKeyPairHandlerImpl();

        KeyPair pair = handler.handle();

        assertThat(pair.getPrivate()).isNotNull();
        assertThat(pair.getPublic().getAlgorithm()).isEqualTo("EC");
    }
}
