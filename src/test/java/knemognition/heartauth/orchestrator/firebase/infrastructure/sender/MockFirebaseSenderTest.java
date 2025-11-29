package knemognition.heartauth.orchestrator.firebase.infrastructure.sender;

import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.sender.MockFirebaseSender;
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

        assertThatCode(() -> sender.sendMessage("token", message))
                .doesNotThrowAnyException();
    }
}
