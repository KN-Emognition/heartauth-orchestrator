//package knemognition.heartauth.orchestrator.internal.app.impl;
//
//import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
//import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
//import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
//import knemognition.heartauth.orchestrator.internal.app.mapper.InternalChallengeMapper;
//import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
//import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
//import knemognition.heartauth.orchestrator.internal.app.ports.out.PushSender;
//import knemognition.heartauth.orchestrator.internal.config.challenge.InternalChallengeProperties;
//import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
//import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
//import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
//import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
//import knemognition.heartauth.orchestrator.shared.utils.NonceUtils;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import test.config.HeartauthUnitTest;
//
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.time.Duration;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class CreateChallengeServiceImplTest extends HeartauthUnitTest {
//
//    @Mock
//    InternalChallengeProperties props;
//    @Mock
//    SecureRandom secureRandom;
//    @Mock
//    DeviceCredentialStore deviceCredentialStore;
//    @Mock
//    PushSender firebaseSender;
//    @Mock
//    InternalChallengeMapper mapper;
//    @Mock
//    InternalChallengeStore<CreateChallenge> flowStore;
//
//    @InjectMocks
//    InternalChallengeServiceImpl service;
//
//    @Test
//    @Disabled
//    void create_happyPath_withRealDomainObjects() {
//        UUID userId = UUID.randomUUID();
//        ChallengeCreateRequest request = ChallengeCreateRequest.builder()
//                .userId(userId).ttlSeconds(45).build();
//
//        when(props.getMinTtl()).thenReturn(10);
//        when(props.getMaxTtl()).thenReturn(120);
//        when(props.getDefaultTtl()).thenReturn(60);
//        when(props.getNonceLength()).thenReturn(24);
//        when(deviceCredentialStore.getDeviceCredentials(userId)).thenReturn(List.of(new DeviceCredential()));
//
//        CreateChallenge createChallenge =
//                CreateChallenge.builder().userId(userId).nonceB64("NONCE_B64").ttlSeconds(45L).build();
//
//        UUID flowId = UUID.randomUUID();
//        CreatedFlowResult createdFlow =
//                CreatedFlowResult.builder().id(flowId).ttlSeconds(45L).build();
//
//        MessageData messageData =
//                MessageData.builder().challengeId(flowId).nonce("NONCE_B64").build();
//
//        ChallengeCreateResponse expectedResponse =
//                ChallengeCreateResponse.builder().challengeId(flowId).build();
//        PublicKey publicKey = mock(PublicKey.class);
//        PrivateKey privateKey = mock(PrivateKey.class);
//        try (MockedStatic<NonceUtils> mocked = Mockito.mockStatic(NonceUtils.class)) {
//            mocked.when(() -> NonceUtils.createNonce(secureRandom, 24)).thenReturn("NONCE_B64");
//
////            when(mapper.toCreateChallenge(request, "NONCE_B64", 45, privateKey)).thenReturn(createChallenge);
//            when(flowStore.createFlow(createChallenge)).thenReturn(createdFlow);
//            when(mapper.toMessageData(createdFlow, "NONCE_B64", publicKey)).thenReturn(messageData);
//            when(mapper.toCreateChallengeResponse(createdFlow)).thenReturn(expectedResponse);
//
//            ChallengeCreateResponse out = service.createChallenge(request);
//            assertSame(expectedResponse, out);
//
//            // Verify behavior
////            verify(mapper).toCreateChallenge(request, "NONCE_B64", 45, privateKey);
//            verify(flowStore).createFlow(createChallenge);
//            verify(firebaseSender).sendData(eq("t1"), eq(messageData), eq(Duration.ofSeconds(45)));
//            verify(firebaseSender).sendData(eq("t2"), eq(messageData), eq(Duration.ofSeconds(45)));
//            verify(mapper).toCreateChallengeResponse(createdFlow);
//        }
//    }
//
//    @Test
//    void create_noActiveDevices_throws() {
//        UUID userId = UUID.randomUUID();
//        ChallengeCreateRequest request = new ChallengeCreateRequest(userId);
//        when(deviceCredentialStore.getDeviceCredentials(userId)).thenReturn(List.of());
//
//        NoActiveDeviceException ex = assertThrows(NoActiveDeviceException.class, () -> service.createChallenge(request));
//        assertEquals("No active device", ex.getMessage());
//
//        verifyNoInteractions(mapper, flowStore, firebaseSender);
//    }
//
//}
