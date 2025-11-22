package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.api.GetPairingStatusCmd;
import knemognition.heartauth.orchestrator.pairings.api.PairingStatusRead;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
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
class GetPairingStatusHandlerTest {

    @Mock
    private PairingStore pairingStore;
    @Mock
    private PairingsMapper mapper;

    @InjectMocks
    private GetPairingStatusHandler handler;

    @Test
    void shouldReturnMappedStatusWhenTenantMatches() {
        UUID tenantId = UUID.randomUUID();
        UUID pairingId = UUID.randomUUID();
        var cmd = GetPairingStatusCmd.builder()
                .tenantId(tenantId)
                .challengeId(pairingId)
                .build();
        var state = PairingState.builder()
                .id(pairingId)
                .tenantId(tenantId)
                .status(FlowStatus.CREATED)
                .build();
        var read = PairingStatusRead.builder()
                .status(FlowStatus.CREATED)
                .build();
        when(pairingStore.getFlow(pairingId)).thenReturn(Optional.of(state));
        when(mapper.toRead(state)).thenReturn(read);

        assertThat(handler.handle(cmd)).isEqualTo(read);
    }

    @Test
    void shouldReturnNotFoundWhenTenantCannotAccess() {
        UUID tenantId = UUID.randomUUID();
        UUID pairingId = UUID.randomUUID();
        var cmd = GetPairingStatusCmd.builder()
                .tenantId(tenantId)
                .challengeId(pairingId)
                .build();
        var state = PairingState.builder()
                .id(pairingId)
                .tenantId(UUID.randomUUID())
                .status(FlowStatus.CREATED)
                .build();
        when(pairingStore.getFlow(pairingId)).thenReturn(Optional.of(state));

        PairingStatusRead result = handler.handle(cmd);

        assertThat(result.getStatus()).isEqualTo(FlowStatus.NOT_FOUND);
        verify(mapper, never()).toRead(any());
    }
}
