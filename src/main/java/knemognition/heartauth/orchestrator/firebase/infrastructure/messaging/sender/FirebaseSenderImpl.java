package knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.sender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import knemognition.heartauth.orchestrator.firebase.api.FirebaseSendException;
import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
import knemognition.heartauth.orchestrator.firebase.api.RefDataCollectPushMessage;
import knemognition.heartauth.orchestrator.firebase.domain.MessageTitleDesc;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.FirebaseSender;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.mappers.FirebaseMapper;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile(SpringProfiles.FCM)
public class FirebaseSenderImpl implements FirebaseSender {

    private final Map<Class<?>, MessageTitleDesc> MESSAGE_DESCRIPTORS = Map.of(
            ChallengePushMessage.class,
            new MessageTitleDesc("Heartauth Login", "Please complete challenge"),
            RefDataCollectPushMessage.class,
            new MessageTitleDesc("Heartauth ECG", "Please collect reference data")
    );

    private final ObjectMapper objectMapper;
    private final FirebaseMessaging firebaseMessaging;
    private final FirebaseMapper firebaseMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String token, Object message) {
        if (token == null || token.isBlank()) {
            throw new FirebaseSendException("FCM token is null/blank");
        }

        long ttlSeconds;
        Object dto;
        Class<?> messageClass;

        if (message instanceof ChallengePushMessage m) {
            ttlSeconds = m.getTtl();
            dto = firebaseMapper.toDto(m);
            messageClass = ChallengePushMessage.class;
        } else if (message instanceof RefDataCollectPushMessage m) {
            ttlSeconds = m.getTtl();
            dto = firebaseMapper.toDto(m);
            messageClass = RefDataCollectPushMessage.class;
        } else {
            throw new FirebaseSendException("Unsupported message type: " + message.getClass());
        }

        if (ttlSeconds < 0) {
            throw new FirebaseSendException("ttl must be non-negative");
        }

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setTtl(Duration.ofSeconds(ttlSeconds)
                        .toMillis())
                .build();

        Map<String, String> data = objectMapper.convertValue(dto, new TypeReference<>() {
        });

        MessageTitleDesc messageTitleDesc = MESSAGE_DESCRIPTORS.get(messageClass);
        if (messageTitleDesc == null) {
            throw new FirebaseSendException("No message descriptor for type: " + messageClass);
        }

        Message toSend = Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(Notification.builder()
                        .setTitle(messageTitleDesc.title())
                        .setBody(messageTitleDesc.description())
                        .build())
                .setAndroidConfig(androidConfig)
                .build();

        try {
            firebaseMessaging.send(toSend);
            log.info("[FIREBASE] Message sent successfully: {}", messageClass.getSimpleName());
        } catch (FirebaseMessagingException e) {
            log.info("[FIREBASE] Message send failed");
            throw new FirebaseSendException("Message send failed.");
        }
    }
}

