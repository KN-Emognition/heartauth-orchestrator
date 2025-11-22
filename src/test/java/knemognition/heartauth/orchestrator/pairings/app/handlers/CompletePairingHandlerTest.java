package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.pairings.api.CompletePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.api.QrCodeClaims;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.app.utils.QrClaimsProvider;
import knemognition.heartauth.orchestrator.pairings.domain.EcgRefTokenClaims;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.pairings.domain.StatusChange;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.SaveUserDeviceCmd;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletePairingHandlerTest {

    @Mock
    private SecurityModule securityModule;
    @Mock
    private UserModule userModule;
    @Mock
    private QrClaimsProvider qrClaimsProvider;
    @Mock
    private EcgModule ecgModule;
    @Mock
    private PairingStore pairingStore;
    @Mock
    private PairingsMapper mapper;

    @InjectMocks
    private CompletePairingHandler handler;

    @Test
    void shouldCompletePairingAndPersistArtifacts() throws Exception {
        UUID pairingId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        var qrClaims = QrCodeClaims.builder()
                .jti(pairingId)
                .tenantId(tenantId)
                .build();
        var state = PairingState.builder()
                .id(pairingId)
                .tenantId(tenantId)
                .status(FlowStatus.PENDING)
                .nonceB64("nonce")
                .publicKey("pub")
                .userId(UUID.randomUUID())
                .build();
        var cmd = CompletePairingCmd.builder()
                .dataToken("data-token")
                .signature("signature")
                .build();
        var validateNonceCmd = ValidateNonceCmd.builder()
                .nonce("nonce")
                .signature("signature")
                .pub("pub")
                .build();
        var userIdentity = IdentifiableUserCmd.builder()
                .tenantId(tenantId)
                .userId(state.getUserId())
                .build();
        var device = DeviceCreate.builder()
                .deviceId("device-1")
                .build();
        var ecgClaims = EcgRefTokenClaims.builder()
                .refEcg(List.of(List.of(0.1f, 0.2f)))
                .build();
        var savedUser = UserRead.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .build();

        when(qrClaimsProvider.handle()).thenReturn(qrClaims);
        when(pairingStore.getFlow(pairingId)).thenReturn(Optional.of(state));
        when(mapper.toCmd(cmd, state)).thenReturn(validateNonceCmd);
        when(mapper.toCmd(state)).thenReturn(userIdentity);
        when(mapper.toDevice(state)).thenReturn(device);
        when(securityModule.decryptJwe(any())).thenReturn(ecgClaims);
        when(userModule.saveUserDevice(any())).thenReturn(savedUser);
        doNothing().when(pairingStore).setStatusOrThrow(any(StatusChange.class));

        handler.handle(cmd);

        verify(securityModule).validateNonce(validateNonceCmd);
        ArgumentCaptor<SaveUserDeviceCmd> saveCaptor = ArgumentCaptor.forClass(SaveUserDeviceCmd.class);
        verify(userModule).saveUserDevice(saveCaptor.capture());
        assertThat(saveCaptor.getValue().getUser()).isEqualTo(userIdentity);
        assertThat(saveCaptor.getValue().getDevice()).isEqualTo(device);

        ArgumentCaptor<SaveReferenceDataCmd> ecgCaptor = ArgumentCaptor.forClass(SaveReferenceDataCmd.class);
        verify(ecgModule).saveReferenceData(ecgCaptor.capture());
        assertThat(ecgCaptor.getValue().getUserId()).isEqualTo(savedUser.getId());
        assertThat(ecgCaptor.getValue().getRefEcg()).isEqualTo(ecgClaims.getRefEcg());

        verify(pairingStore).setStatusOrThrow(any(StatusChange.class));
    }
}
