package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.FlowStatusReason;
import knemognition.heartauth.orchestrator.challenges.domain.StatusChange;
import knemognition.heartauth.orchestrator.modelapi.api.CompleteChallengeWithPredictionResultCmd;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModelApiCallbackHandlerTest {

    private static final UUID CHALLENGE_ID = UUID.fromString("c8d4a248-1440-4a9c-a2d7-99c37b35ec77");
    private static final UUID CORRELATION_ID = UUID.fromString("a9c2015c-2702-4d7f-9eab-09fe32b57244");

    @Mock
    private ChallengeStore challengeStore;

    @InjectMocks
    private ModelApiCallbackHandler handler;

    @Test
    void shouldMarkChallengeDeniedWhenModelReturnsError() {
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .status(FlowStatus.PENDING)
                .build();
        when(challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID)).thenReturn(state);
        var cmd = CompleteChallengeWithPredictionResultCmd.builder()
                .correlationId(CORRELATION_ID)
                .error("timeout")
                .score(0.12f)
                .build();

        handler.handle(cmd);

        var captor = ArgumentCaptor.forClass(StatusChange.class);
        verify(challengeStore).setStatus(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(FlowStatus.DENIED);
        assertThat(captor.getValue().getReason()).isEqualTo(FlowStatusReason.FLOW_DENIED_DUE_TO_MODEL_API_ERROR);
    }

    @Test
    void shouldApproveChallengeWhenPredictionIsSuccessful() {
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .status(FlowStatus.PENDING)
                .build();
        when(challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID)).thenReturn(state);
        var cmd = CompleteChallengeWithPredictionResultCmd.builder()
                .correlationId(CORRELATION_ID)
                .prediction(true)
                .score(0.91f)
                .build();

        handler.handle(cmd);

        var captor = ArgumentCaptor.forClass(StatusChange.class);
        verify(challengeStore).setStatus(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(FlowStatus.APPROVED);
        assertThat(captor.getValue().getReason()).isEqualTo(FlowStatusReason.FLOW_COMPLETED_SUCCESSFULLY_WITH_AUTHENTICATION);
    }

    @Test
    void shouldDenyChallengeWhenPredictionIsRejected() {
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .status(FlowStatus.PENDING)
                .build();
        when(challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID)).thenReturn(state);
        var cmd = CompleteChallengeWithPredictionResultCmd.builder()
                .correlationId(CORRELATION_ID)
                .prediction(false)
                .score(0.22f)
                .build();

        handler.handle(cmd);

        var captor = ArgumentCaptor.forClass(StatusChange.class);
        verify(challengeStore).setStatus(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(FlowStatus.DENIED);
        assertThat(captor.getValue().getReason()).isEqualTo(FlowStatusReason.FLOW_DENIED_WITH_AUTHENTICATION_FAILURE);
    }

    @Test
    void shouldIgnoreCallbacksForNonPendingStates() {
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .status(FlowStatus.APPROVED)
                .build();
        when(challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID)).thenReturn(state);
        var cmd = CompleteChallengeWithPredictionResultCmd.builder()
                .correlationId(CORRELATION_ID)
                .prediction(true)
                .build();

        handler.handle(cmd);

        verify(challengeStore, never()).setStatus(any());
    }
}
