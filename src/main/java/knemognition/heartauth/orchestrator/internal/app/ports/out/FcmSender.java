package knemognition.heartauth.orchestrator.internal.app.ports.out;


import java.time.Duration;
import java.util.Map;

public interface FcmSender {
    void sendData(String fcmToken, Map<String, String> data, Duration ttl);
}
