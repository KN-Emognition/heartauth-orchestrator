package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.ChallengeStatusRead;
import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.api.GetChallengeStatusCmd;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetChallengeStatusHandlerTest {

    private static final UUID TENANT_ID = UUID.fromString("910a7b07-26f3-4b42-98ca-3453e3da4d93");
    private static final UUID CHALLENGE_ID = UUID.fromString("775a5acb-1f22-4955-98af-25f264922cc4");

    @Mock
    private ChallengeStore challengeStore;
    @Mock
    private ChallengesMapper challengesMapper;
    @InjectMocks
    private GetChallengeStatusHandler handler;

    @Test
    void shouldReturnMappedStateWhenTenantMatches() {
        var cmd = GetChallengeStatusCmd.builder()
                .tenantId(TENANT_ID)
                .challengeId(CHALLENGE_ID)
                .build();
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .tenantId(TENANT_ID)
                .status(FlowStatus.APPROVED)
                .build();
        var read = ChallengeStatusRead.builder()
                .status(FlowStatus.APPROVED)
                .reason("approved")
                .build();
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(Optional.of(state));
        when(challengesMapper.toRead(state)).thenReturn(read);

        var result = handler.handle(cmd);

        assertThat(result).isEqualTo(read);
    }

    @Test
    void shouldReturnNotFoundWhenStateMissing() {
        var cmd = GetChallengeStatusCmd.builder()
                .tenantId(TENANT_ID)
                .challengeId(CHALLENGE_ID)
                .build();
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(Optional.empty());

        var result = handler.handle(cmd);

        assertThat(result.getStatus()).isEqualTo(FlowStatus.NOT_FOUND);
        verify(challengesMapper, never()).toRead(any(ChallengeState.class));
    }

    @Test
    void shouldReturnNotFoundWhenTenantDoesNotMatch() {
        var cmd = GetChallengeStatusCmd.builder()
                .tenantId(TENANT_ID)
                .challengeId(CHALLENGE_ID)
                .build();
        var foreignState = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .tenantId(UUID.fromString("e1f8d88b-9340-4010-95bf-7d86b5edc0f5"))
                .status(FlowStatus.CREATED)
                .build();
        when(challengeStore.getFlow(CHALLENGE_ID)).thenReturn(Optional.of(foreignState));

        var result = handler.handle(cmd);

        assertThat(result.getStatus()).isEqualTo(FlowStatus.NOT_FOUND);
        verify(challengesMapper, never()).toRead(foreignState);
    }
}
