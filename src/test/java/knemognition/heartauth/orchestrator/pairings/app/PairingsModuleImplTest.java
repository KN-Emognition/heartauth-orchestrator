package knemognition.heartauth.orchestrator.pairings.app;

import knemognition.heartauth.orchestrator.pairings.api.*;
import knemognition.heartauth.orchestrator.pairings.app.handlers.CompletePairingHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.CreatePairingHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.GetPairingStatusHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.InitPairingHandler;
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
class PairingsModuleImplTest {

    @Mock
    private GetPairingStatusHandler statusHandler;
    @Mock
    private CreatePairingHandler createHandler;
    @Mock
    private InitPairingHandler initHandler;
    @Mock
    private CompletePairingHandler completeHandler;

    @InjectMocks
    private PairingsModuleImpl module;

    @Test
    void shouldDelegateStatusLookup() {
        var cmd = GetPairingStatusCmd.builder()
                .tenantId(UUID.randomUUID())
                .challengeId(UUID.randomUUID())
                .build();
        var read = PairingStatusRead.builder()
                .status(FlowStatus.PENDING)
                .build();
        when(statusHandler.handle(cmd)).thenReturn(read);

        assertThat(module.getStatus(cmd)).isEqualTo(read);
    }

    @Test
    void shouldDelegateCreate() {
        var cmd = CreatePairingCmd.builder()
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();
        var read = CreatedPairingRead.builder()
                .jti(UUID.randomUUID())
                .build();
        when(createHandler.createPairing(cmd)).thenReturn(read);

        assertThat(module.create(cmd)).isEqualTo(read);
    }

    @Test
    void shouldDelegateInit() {
        var cmd = InitPairingCmd.builder()
                .publicKey("pub")
                .build();
        var read = InitPairingRead.builder()
                .nonce("nonce")
                .build();
        when(initHandler.handle(cmd)).thenReturn(read);

        assertThat(module.init(cmd)).isEqualTo(read);
    }

    @Test
    void shouldDelegateComplete() {
        var cmd = CompletePairingCmd.builder()
                .dataToken("token")
                .signature("sig")
                .build();

        module.complete(cmd);

        verify(completeHandler).handle(cmd);
    }
}
