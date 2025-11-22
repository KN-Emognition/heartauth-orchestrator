package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.api.QrCodeClaims;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.app.utils.QrClaimsProvider;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.pairings.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.pairings.domain.StatusChange;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.users.api.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitPairingHandlerTest {

    @Mock
    private SecurityModule securityModule;
    @Mock
    private QrClaimsProvider qrClaimsProvider;
    @Mock
    private PairingProperties pairingProperties;
    @Mock
    private PairingStore pairingStore;
    @Mock
    private PairingsMapper mapper;

    @InjectMocks
    private InitPairingHandler handler;

    @Test
    void shouldInitializePairingWhenStateIsValid() {
        UUID tenantId = UUID.randomUUID();
        UUID pairingId = UUID.randomUUID();
        var cmd = InitPairingCmd.builder()
                .publicKey("PEM")
                .deviceId("device-1")
                .platform(Platform.ANDROID)
                .build();
        var qrClaims = QrCodeClaims.builder()
                .jti(pairingId)
                .tenantId(tenantId)
                .exp(1234L)
                .build();
        var state = PairingState.builder()
                .id(pairingId)
                .tenantId(tenantId)
                .status(FlowStatus.CREATED)
                .exp(9999L)
                .build();
        var enrich = EnrichDeviceData.builder()
                .jti(pairingId)
                .nonceB64("nonce")
                .build();
        when(qrClaimsProvider.handle()).thenReturn(qrClaims);
        when(pairingProperties.getNonceLength()).thenReturn(32);
        when(securityModule.createNonce(32)).thenReturn("nonce");
        when(pairingStore.getFlow(pairingId)).thenReturn(Optional.of(state));
        when(mapper.toEnrichDeviceData(cmd, "nonce", pairingId)).thenReturn(enrich);
        doNothing().when(pairingStore).setStatusOrThrow(any(StatusChange.class));

        InitPairingRead result = handler.handle(cmd);

        assertThat(result.getNonce()).isEqualTo("nonce");
        assertThat(result.getExpiresAt()).isEqualTo(state.getExp());
        verify(pairingStore).enrichWithDeviceData(enrich);
        verify(pairingStore).setStatusOrThrow(any(StatusChange.class));
    }

    @Test
    void shouldThrowWhenTenantMismatch() {
        UUID pairingId = UUID.randomUUID();
        var cmd = InitPairingCmd.builder()
                .publicKey("PEM")
                .deviceId("device")
                .build();
        var qrClaims = QrCodeClaims.builder()
                .jti(pairingId)
                .tenantId(UUID.randomUUID())
                .build();
        var state = PairingState.builder()
                .id(pairingId)
                .tenantId(UUID.randomUUID())
                .status(FlowStatus.CREATED)
                .build();
        when(qrClaimsProvider.handle()).thenReturn(qrClaims);
        when(pairingProperties.getNonceLength()).thenReturn(32);
        when(securityModule.createNonce(32)).thenReturn("nonce");
        when(pairingStore.getFlow(pairingId)).thenReturn(Optional.of(state));

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(NoPairingException.class);
    }
}
