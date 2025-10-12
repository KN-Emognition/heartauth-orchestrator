package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm;

import com.google.firebase.messaging.*;
import knemognition.heartauth.orchestrator.internal.app.ports.out.PushSender;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.FirebaseSendException;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengePushMessage;
import knemognition.heartauth.orchestrator.shared.app.mapper.RecordMapper;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!" + SpringProfiles.FCM_MOCK)
public class FirebaseSenderImpl implements PushSender {

    private final RecordMapper recordMapper;
    private final FirebaseMessaging firebaseMessaging;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendData(String token, ChallengePushMessage messageData) {
        if (token == null || token.isBlank()) {
            throw new FirebaseSendException("FCM token is null/blank");
        }
        Duration ttl = Duration.ofSeconds(messageData.getTtl());
        if (ttl != null && ttl.isNegative()) {
            throw new FirebaseSendException("ttl must be non-negative");
        }
        AndroidConfig.Builder android = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH);
        android.setTtl(Objects.requireNonNull(ttl)
                .toMillis());

        Map<String, String> data = recordMapper.convertObjectToMap(messageData);
        Message message = createMessage(token, data, android);
        try {
            firebaseMessaging.send(message);
            log.info("Message sent successfully");
        } catch (FirebaseMessagingException e) {
            throw new FirebaseSendException("Message send failed.");
        }
    }

    private Message createMessage(String token, Map<String, String> data, AndroidConfig.Builder android) {
        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(Notification.builder()
                        .setTitle("Heartauth Login")
                        .setBody("Click to complete login!")
                        .build())
                .setAndroidConfig(android.build())
                .build();
    }
}
