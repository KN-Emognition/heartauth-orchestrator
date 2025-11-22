package knemognition.heartauth.orchestrator.security.app.handlers;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNonceHandlerTest {

    @Test
    void shouldProduceBase64NonceOfRequestedSize() {
        CreateNonceHandler handler = new CreateNonceHandler(new SecureRandom());

        String nonce = handler.handle(32);

        assertThat(nonce).isNotBlank();
        assertThat(java.util.Base64.getDecoder().decode(nonce)).hasSize(32);
    }
}
