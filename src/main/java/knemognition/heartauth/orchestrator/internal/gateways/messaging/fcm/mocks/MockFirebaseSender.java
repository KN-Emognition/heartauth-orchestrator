package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm.mocks;

import knemognition.heartauth.orchestrator.internal.app.ports.out.PushSender;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengePushMessage;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Primary
@Profile(SpringProfiles.FCM_MOCK)
public class MockFirebaseSender implements PushSender {

    @Override
    public void sendData(String token, ChallengePushMessage messageData) {
        log.warn("Mock sendData called with token");
    }

}
