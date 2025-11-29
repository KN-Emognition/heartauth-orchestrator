package knemognition.heartauth.orchestrator.firebase.app;

import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
import knemognition.heartauth.orchestrator.firebase.api.FirebaseModule;
import knemognition.heartauth.orchestrator.firebase.api.RefDataCollectPushMessage;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.FirebaseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebaseModuleImpl implements FirebaseModule {

    private final FirebaseSender firebaseSender;

    @Override
    public void sendChallengeMessage(String fcmToken, ChallengePushMessage data) {
        firebaseSender.sendMessage(fcmToken, data);
    }

    @Override
    public void sendRefDataCollectMessage(String fcmToken, RefDataCollectPushMessage data) {
        firebaseSender.sendMessage(fcmToken, data);
    }
}
