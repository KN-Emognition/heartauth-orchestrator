package knemognition.heartauth.orchestrator.challenges.app;

import knemognition.heartauth.orchestrator.challenges.api.*;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CompleteChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CreateChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.GetChallengeStatusHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengesModuleImplTest {

    @Mock
    private GetChallengeStatusHandler getHandler;
    @Mock
    private CreateChallengeHandler createHandler;
    @Mock
    private CompleteChallengeHandler completeHandler;

    @InjectMocks
    private ChallengesModuleImpl module;

    @Test
    void shouldDelegateStatusRequests() {
        var cmd = GetChallengeStatusCmd.builder()
                .tenantId(UUID.randomUUID())
                .challengeId(UUID.randomUUID())
                .build();
        var read = ChallengeStatusRead.builder()
                .status(FlowStatus.CREATED)
                .build();
        when(getHandler.handle(cmd)).thenReturn(read);

        ChallengeStatusRead result = module.getStatus(cmd);

        assertThat(result).isEqualTo(read);
        verify(getHandler).handle(cmd);
    }

    @Test
    void shouldDelegateChallengeCreation() {
        var cmd = CreateChallengeCmd.builder()
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();
        var read = CreatedChallengeRead.builder()
                .challengeId(UUID.randomUUID())
                .build();
        when(createHandler.handle(cmd)).thenReturn(read);

        CreatedChallengeRead result = module.create(cmd);

        assertThat(result).isEqualTo(read);
        verify(createHandler).handle(cmd);
    }

    @Test
    void shouldDelegateChallengeCompletion() {
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(UUID.randomUUID())
                .build();
        when(completeHandler.handle(cmd)).thenReturn(true);

        boolean completed = module.complete(cmd);

        assertThat(completed).isTrue();
        verify(completeHandler).handle(cmd);
    }
}
