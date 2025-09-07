package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;

import java.time.Duration;

public interface FirebaseSender {
    void sendData(String fcmToken, MessageData data, Duration ttl);
}
