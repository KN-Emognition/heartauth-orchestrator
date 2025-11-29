package knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.sender;

import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.FirebaseSender;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Profile("!" + SpringProfiles.FCM)
public class MockFirebaseSender implements FirebaseSender {

    @Override
    public <T> void sendMessage(String token, T message) {
        log.warn("[FIREBASE] Mock sendData called with token {} and message {}", token, message);
    }
}
