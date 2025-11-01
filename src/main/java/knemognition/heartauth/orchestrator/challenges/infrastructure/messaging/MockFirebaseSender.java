package knemognition.heartauth.orchestrator.challenges.infrastructure.messaging;

import knemognition.heartauth.orchestrator.challenges.app.ports.PushSender;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengePushMessage;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Profile("!" + SpringProfiles.FCM)
public class MockFirebaseSender implements PushSender {

    @Override
    public void sendData(String token, ChallengePushMessage messageData) {
        log.warn("Mock sendData called with token");
    }

}
