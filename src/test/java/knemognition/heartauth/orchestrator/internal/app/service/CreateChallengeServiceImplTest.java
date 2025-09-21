package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreateChallengeMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FindFcmTokensStore;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FirebaseSender;
import knemognition.heartauth.orchestrator.internal.config.challenge.InternalChallengeProperties;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.shared.utils.NonceUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import test.config.HeartauthUnitTest;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreateChallengeServiceImplTest extends HeartauthUnitTest {

    @Mock
    InternalChallengeProperties props;
    @Mock
    SecureRandom secureRandom;
    @Mock
    FindFcmTokensStore deviceCredentialStore;
    @Mock
    FirebaseSender firebaseSender;
    @Mock
    CreateChallengeMapper mapper;
    @Mock
    CreateFlowStore<CreateChallenge> flowStore;

    @InjectMocks
    CreateChallengeServiceImpl service;

    @Test
    void create_happyPath_withRealDomainObjects() {
        UUID userId = UUID.randomUUID();
        ChallengeCreateRequest request = ChallengeCreateRequest.builder()
                .userId(userId).ttlSeconds(45).build();

        when(props.getMinTtl()).thenReturn(10);
        when(props.getMaxTtl()).thenReturn(120);
        when(props.getDefaultTtl()).thenReturn(60);
        when(props.getNonceLength()).thenReturn(24);
        when(deviceCredentialStore.getActiveFcmTokens(userId)).thenReturn(List.of("t1", "t2"));

        CreateChallenge createChallenge =
                CreateChallenge.builder().userId(userId).nonceB64("NONCE_B64").ttlSeconds(45L).build();

        UUID flowId = UUID.randomUUID();
        CreatedFlowResult createdFlow =
                CreatedFlowResult.builder().id(flowId).ttl(45L).build();

        MessageData messageData =
                MessageData.builder().challengeId(flowId).nonce("NONCE_B64").build();

        ChallengeCreateResponse expectedResponse =
                ChallengeCreateResponse.builder().challengeId(flowId).build();

        try (MockedStatic<NonceUtils> mocked = Mockito.mockStatic(NonceUtils.class)) {
            mocked.when(() -> NonceUtils.createNonce(secureRandom, 24)).thenReturn("NONCE_B64");

            when(mapper.toCreateChallenge(request, "NONCE_B64", 45)).thenReturn(createChallenge);
            when(flowStore.create(createChallenge)).thenReturn(createdFlow);
            when(mapper.toMessageData(createdFlow, "NONCE_B64")).thenReturn(messageData);
            when(mapper.toResponse(createdFlow)).thenReturn(expectedResponse);

            ChallengeCreateResponse out = service.create(request);
            assertSame(expectedResponse, out);

            // Verify behavior
            verify(mapper).toCreateChallenge(request, "NONCE_B64", 45);
            verify(flowStore).create(createChallenge);
            verify(firebaseSender).sendData(eq("t1"), eq(messageData), eq(Duration.ofSeconds(45)));
            verify(firebaseSender).sendData(eq("t2"), eq(messageData), eq(Duration.ofSeconds(45)));
            verify(mapper).toResponse(createdFlow);
        }
    }

    @Test
    void create_noActiveDevices_throws() {
        UUID userId = UUID.randomUUID();
        ChallengeCreateRequest request = new ChallengeCreateRequest(userId);
        when(deviceCredentialStore.getActiveFcmTokens(userId)).thenReturn(List.of());

        NoActiveDeviceException ex = assertThrows(NoActiveDeviceException.class, () -> service.create(request));
        assertEquals("No active device", ex.getMessage());

        verifyNoInteractions(mapper, flowStore, firebaseSender);
    }

}
