package knemognition.heartauth.orchestrator.challenges.infrastructure.messaging;

import knemognition.heartauth.orchestrator.challenges.domain.ChallengePushMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class MockFirebaseSenderTest {

    @Test
    void shouldNoopSendRequests() {
        var sender = new MockFirebaseSender();
        var message = ChallengePushMessage.builder()
                .challengeId(UUID.randomUUID())
                .ttl(10L)
                .build();

        assertThatCode(() -> sender.sendData("token", message))
                .doesNotThrowAnyException();
    }
}
