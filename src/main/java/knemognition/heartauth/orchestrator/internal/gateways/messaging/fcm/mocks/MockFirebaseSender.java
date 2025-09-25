package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm.mocks;

import com.google.firebase.messaging.*;
import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FirebaseSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@Primary
@Profile("dev")
public class MockFirebaseSender implements FirebaseSender {

    @Override
    public void sendData(String token, MessageData messageData, Duration ttl) {
        log.warn("Mock sendData called with token: {}, publicKey: {}", token, messageData.getPublicKey());
    }

}
