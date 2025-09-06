package knemognition.heartauth.orchestrator.internal.app.ports.out;


import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.Duration;
import java.util.Map;

public interface FirebaseSender {
    void sendData(String fcmToken, Map<String, String> data, Duration ttl) throws FirebaseMessagingException;
}
