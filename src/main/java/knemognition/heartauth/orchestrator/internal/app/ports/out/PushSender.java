package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.internal.app.domain.SendPushMessage;

import java.time.Duration;

public interface PushSender {
    void sendData(String fcmToken, SendPushMessage data, Duration ttl);
}
