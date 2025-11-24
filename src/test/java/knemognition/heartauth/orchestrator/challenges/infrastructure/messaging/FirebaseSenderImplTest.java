//package knemognition.heartauth.orchestrator.challenges.infrastructure.messaging;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import knemognition.heartauth.orchestrator.challenges.api.FirebaseSendException;
//import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
//import knemognition.heartauth.orchestrator.firebase.domain.MessageType;
//import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.sender.FirebaseSenderImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FirebaseSenderImplTest {
//
//    @Mock
//    private FirebaseMessaging firebaseMessaging;
//
//    private FirebaseSenderImpl sender;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        sender = new FirebaseSenderImpl(objectMapper, firebaseMessaging);
//    }
//
//    @Test
//    void shouldSendMessageWithSerializedPayload() throws Exception {
//        ChallengePushMessage payload = ChallengePushMessage.builder()
//                .challengeId(UUID.fromString("9f6830fd-18b4-465d-94e0-f45f5ab9d418"))
//                .publicKey("pem")
//                .nonce("nonce")
//                .ttl(40L)
//                .exp(Instant.now().plusSeconds(40).getEpochSecond())
//                .build();
//        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id");
//
//        sender.sendData("abc-token", payload);
//
//        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
//        verify(firebaseMessaging).send(captor.capture());
//        assertThat(captor.getValue()).isNotNull();
//    }
//
//    @Test
//    void shouldRejectBlankTokens() {
//        ChallengePushMessage payload = ChallengePushMessage.builder()
//                .challengeId(UUID.randomUUID())
//                .ttl(30L)
//                .build();
//
//        assertThatThrownBy(() -> sender.sendData(" ", payload))
//                .isInstanceOf(FirebaseSendException.class);
//    }
//
//    @Test
//    void shouldRejectNegativeTtl() {
//        ChallengePushMessage payload = ChallengePushMessage.builder()
//                .challengeId(UUID.randomUUID())
//                .ttl(-5L)
//                .build();
//
//        assertThatThrownBy(() -> sender.sendData("token", payload))
//                .isInstanceOf(FirebaseSendException.class);
//    }
//
//    @Test
//    void shouldWrapFirebaseMessagingErrors() throws Exception {
//        ChallengePushMessage payload = ChallengePushMessage.builder()
//                .challengeId(UUID.randomUUID())
//                .type(MessageType.CHALLENGE)
//                .publicKey("pem")
//                .nonce("nonce")
//                .ttl(15L)
//                .exp(Instant.now().plusSeconds(15).getEpochSecond())
//                .build();
//        FirebaseMessagingException firebaseException = mock(FirebaseMessagingException.class);
//        when(firebaseMessaging.send(any(Message.class))).thenThrow(firebaseException);
//
//        assertThatThrownBy(() -> sender.sendData("token", payload))
//                .isInstanceOf(FirebaseSendException.class);
//    }
//}
