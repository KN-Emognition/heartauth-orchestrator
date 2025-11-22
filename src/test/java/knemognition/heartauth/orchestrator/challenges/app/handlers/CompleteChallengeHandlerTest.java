package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.CompleteChallengeWithPredictionPayloadCmd;
import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.EcgTestTokenClaims;
import knemognition.heartauth.orchestrator.challenges.domain.FlowStatusReason;
import knemognition.heartauth.orchestrator.challenges.domain.StatusChange;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiSendApi;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompleteChallengeHandlerTest {

    private static final UUID CHALLENGE_ID = UUID.fromString("043e9fca-98fd-444e-8535-f9d6d1a7916a");
    private static final UUID USER_ID = UUID.fromString("df87aa5e-aef3-4c4c-9cf5-0174deadb305");
    private static final UUID TENANT_ID = UUID.fromString("cabb0a80-9c67-4ffc-8778-570e8af15dc9");
    private static final UUID USER_RECORD_ID = UUID.fromString("2b22cd26-5fd0-4b9c-8224-60155ab25b6d");
    private static final UUID MODEL_TRY_ID = UUID.fromString("c85c53af-e58c-4fab-8a9a-bda8451bcb0a");

    @Mock
    private EcgModule ecgModule;
    @Mock
    private UserModule userModule;
    @Mock
    private SecurityModule securityModule;
    @Mock
    private ModelApiSendApi modelApiSendApi;
    @Mock
    private ChallengesMapper challengesMapper;
    @Mock
    private ChallengeStore challengeStore;

    @InjectMocks
    private CompleteChallengeHandler handler;

    @Test
    void shouldCompleteChallengeAndReturnTrueWhenApprovedStatusArrives() throws Exception {
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(CHALLENGE_ID)
                .dataToken("test-token")
                .signature("signature")
                .build();
        var storedState = baseState(FlowStatus.CREATED);
        var approvedState = baseState(FlowStatus.APPROVED);
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(
                Optional.of(storedState),
                Optional.of(approvedState),
                Optional.of(approvedState));
        var validateNonceCmd = ValidateNonceCmd.builder()
                .nonce("nonce")
                .signature("signature")
                .pub(storedState.getUserPublicKey())
                .build();
        when(challengesMapper.toCmd(cmd, storedState)).thenReturn(validateNonceCmd);
        var userIdentity = IdentifiableUserCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .build();
        when(challengesMapper.toCmd(storedState)).thenReturn(userIdentity);
        var userRead = UserRead.builder()
                .id(USER_RECORD_ID)
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .build();
        when(userModule.getUser(userIdentity)).thenReturn(Optional.of(userRead));
        var refCmd = GetRefDataCmd.builder()
                .userId(USER_RECORD_ID)
                .build();
        var refEcg = RefEcgRead.builder()
                .refEcg(List.of(List.of(0.12f, 0.14f)))
                .build();
        when(ecgModule.getUserReferenceData(refCmd)).thenReturn(refEcg);
        var claims = EcgTestTokenClaims.builder()
                .testEcg(List.of(0.45f, 0.47f))
                .build();
        when(securityModule.decryptJwe(any())).thenReturn(claims);
        when(challengeStore.setStatus(any(StatusChange.class))).thenReturn(true);

        var result = handler.handle(cmd);

        assertThat(result).isTrue();
        verify(securityModule).validateNonce(validateNonceCmd);
        var statusCaptor = ArgumentCaptor.forClass(StatusChange.class);
        verify(challengeStore).setStatus(statusCaptor.capture());
        assertThat(statusCaptor.getValue().getStatus()).isEqualTo(FlowStatus.PENDING);
        assertThat(statusCaptor.getValue().getReason()).isEqualTo(FlowStatusReason.FLOW_WAITING_FOR_MODEL);
        var predictCaptor = ArgumentCaptor.forClass(EcgSendPredictCmd.class);
        verify(modelApiSendApi).handle(predictCaptor.capture());
        var predictCmd = predictCaptor.getValue();
        assertThat(predictCmd.getCorrelationId()).isEqualTo(MODEL_TRY_ID);
        assertThat(predictCmd.getTestEcg()).isEqualTo(claims.getTestEcg());
        assertThat(predictCmd.getRefEcg()).isEqualTo(refEcg.getRefEcg());
    }

    @Test
    void shouldReturnFalseWhenChallengeDoesNotExist() {
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(CHALLENGE_ID)
                .dataToken("missing-challenge")
                .build();
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(Optional.empty());

        var result = handler.handle(cmd);

        assertThat(result).isFalse();
        verify(modelApiSendApi, never()).handle(any());
        verify(challengeStore, never()).setStatus(any());
    }

    @Test
    void shouldReturnFalseWhenFinalStatusIsDenied() throws Exception {
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(CHALLENGE_ID)
                .dataToken("retry-token")
                .signature("signature")
                .build();
        var storedState = baseState(FlowStatus.CREATED);
        var deniedState = baseState(FlowStatus.DENIED);
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(
                Optional.of(storedState),
                Optional.of(deniedState),
                Optional.of(deniedState));
        var validateNonceCmd = ValidateNonceCmd.builder()
                .nonce("nonce")
                .signature("signature")
                .pub(storedState.getUserPublicKey())
                .build();
        when(challengesMapper.toCmd(cmd, storedState)).thenReturn(validateNonceCmd);
        var userIdentity = IdentifiableUserCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .build();
        when(challengesMapper.toCmd(storedState)).thenReturn(userIdentity);
        var userRead = UserRead.builder()
                .id(USER_RECORD_ID)
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .build();
        when(userModule.getUser(userIdentity)).thenReturn(Optional.of(userRead));
        var refCmd = GetRefDataCmd.builder()
                .userId(USER_RECORD_ID)
                .build();
        var refEcg = RefEcgRead.builder()
                .refEcg(List.of(List.of(0.99f)))
                .build();
        when(ecgModule.getUserReferenceData(refCmd)).thenReturn(refEcg);
        var claims = EcgTestTokenClaims.builder()
                .testEcg(List.of(0.15f))
                .build();
        when(securityModule.decryptJwe(any())).thenReturn(claims);
        when(challengeStore.setStatus(any(StatusChange.class))).thenReturn(true);

        var result = handler.handle(cmd);

        assertThat(result).isFalse();
        var statusCaptor = ArgumentCaptor.forClass(StatusChange.class);
        verify(challengeStore).setStatus(statusCaptor.capture());
        assertThat(statusCaptor.getValue().getStatus()).isEqualTo(FlowStatus.PENDING);
    }

    private ChallengeState baseState(FlowStatus status) {
        return ChallengeState.builder()
                .id(CHALLENGE_ID)
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .status(status)
                .ephemeralPrivateKey("ephemeral-key")
                .userPublicKey("public-key")
                .modelApiTryId(MODEL_TRY_ID)
                .nonceB64("nonce")
                .ttlSeconds(60L)
                .build();
    }
}
