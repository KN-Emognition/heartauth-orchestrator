package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm;

import com.google.firebase.messaging.*;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FirebaseSender;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.FirebaseSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseSenderImpl implements FirebaseSender {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendData(String token, Map<String, String> data, Duration ttl) {
        if (token == null || token.isBlank()) {
            throw new FirebaseSendException("FCM token is null/blank");
        }
        if (ttl != null && ttl.isNegative()) {
            throw new FirebaseSendException("ttl must be non-negative");
        }
        AndroidConfig.Builder android = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH);
        android.setTtl(ttl.toMillis());

        ApnsConfig.Builder apns = ApnsConfig.builder();
        long expEpoch = Instant.now().plus(ttl).getEpochSecond();
        apns.putHeader("apns-expiration", String.valueOf(expEpoch));

        Message message = createMessage(token, data, android, apns);

        try {
            firebaseMessaging.send(message);
            log.info("Sent FCM message successfully ");
        } catch (FirebaseMessagingException e) {
            throw new FirebaseSendException("Firebase send exception");
        }
    }

    private Message createMessage(String token, Map<String, String> data, AndroidConfig.Builder android, ApnsConfig.Builder apns) {
        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(Notification.builder()
                        .setTitle("New login attempt")
                        .setBody("Authenticate yourself to complete the login process.")
                        .build())
                .setAndroidConfig(android.build())
                .setApnsConfig(apns.build())
                .build();
    }
}
