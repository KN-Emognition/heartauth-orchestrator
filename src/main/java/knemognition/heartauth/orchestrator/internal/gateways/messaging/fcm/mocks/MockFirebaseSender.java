package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm.mocks;

import knemognition.heartauth.orchestrator.internal.app.domain.SendPushMessage;
import knemognition.heartauth.orchestrator.internal.app.ports.out.PushSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@Primary
@Profile("dev")
public class MockFirebaseSender implements PushSender {

    @Override
    public void sendData(String token, SendPushMessage messageData, Duration ttl) {
        log.warn("Mock sendData called with token: {}", token);
    }

}
