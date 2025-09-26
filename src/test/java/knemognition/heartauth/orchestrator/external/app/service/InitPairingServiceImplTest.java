package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.mapper.InitPairingMapper;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import knemognition.heartauth.orchestrator.external.config.pairing.ExternalPairingProperties;
import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import knemognition.heartauth.orchestrator.external.model.PairingInitResponse;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import test.config.HeartauthUnitTest;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class InitPairingServiceImplTest extends HeartauthUnitTest {

    @Mock
    ExternalPairingProperties externalPairingProperties;
    @Mock
    InitPairingMapper initPairingMapper;
    @Mock
    SecureRandom secureRandom;
    @Mock
    EnrichDeviceDataStore enrichDeviceDataStore;
    @Mock
    StatusStore<PairingState> pairingStateStatusStore;
    @Mock
    PemMapper pemMapper;

    @InjectMocks
    InitPairingServiceImpl service;

    PairingInitRequest request;
    QrClaims qrClaims;
    UUID jti;

    @BeforeEach
    void setUp() {
        request = new PairingInitRequest().publicKeyPem("-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqh...\n-----END PUBLIC KEY-----");
        jti = UUID.randomUUID();

        qrClaims = QrClaims.builder()
                .jti(jti)
                .build();
    }

    @Test
    void init_success() {
        when(externalPairingProperties.getNonceLength()).thenReturn(16);

        doAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) i; // 0x00,0x01,0x02,...
            return null;
        }).when(secureRandom).nextBytes(any(byte[].class));

        FlowStatusDescription created =
                FlowStatusDescription.builder().status(FlowStatus.CREATED).build();
        when(pairingStateStatusStore.getStatus(jti)).thenReturn(Optional.of(created));

        EnrichDeviceData mapped = EnrichDeviceData.builder()
                .jti(jti)
                .nonceB64("will-be-overwritten-by-argcaptor")
                .build();
        ArgumentCaptor<String> nonceCaptor = ArgumentCaptor.forClass(String.class);
        when(initPairingMapper.toEnrichDeviceData(eq(request), nonceCaptor.capture(), eq(jti)))
                .thenReturn(mapped);

        PairingInitResponse response = service.init(request, qrClaims);

        verify(pemMapper).publicMapAndValidate("-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqh...\n-----END PUBLIC KEY-----");

        verify(enrichDeviceDataStore).enrich(mapped);

        ArgumentCaptor<StatusChange> statusCaptor = ArgumentCaptor.forClass(StatusChange.class);
        verify(pairingStateStatusStore).setStatusOrThrow(statusCaptor.capture());
        assertThat(statusCaptor.getValue().getId()).isEqualTo(jti);
        assertThat(statusCaptor.getValue().getStatus()).isEqualTo(FlowStatus.PENDING);

        String nonceUsed = nonceCaptor.getValue();
        assertThat(nonceUsed).isNotBlank();
        assertThat(response.getNonce()).isEqualTo(nonceUsed);
    }

    @Test
    void init_throws_when_status_not_found() {
        when(externalPairingProperties.getNonceLength()).thenReturn(16);
        when(pairingStateStatusStore.getStatus(jti)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.init(request, qrClaims))
                .isInstanceOf(StatusServiceException.class)
                .hasMessageContaining("Status not found");

        verify(enrichDeviceDataStore, never()).enrich(any());
        verify(pairingStateStatusStore, never()).setStatusOrThrow(any());
    }

    @Test
    void init_throws_when_status_not_created() {
        when(externalPairingProperties.getNonceLength()).thenReturn(16);

        FlowStatusDescription notCreated =
                FlowStatusDescription.builder().status(FlowStatus.PENDING).build();
        when(pairingStateStatusStore.getStatus(jti)).thenReturn(Optional.of(notCreated));

        assertThatThrownBy(() -> service.init(request, qrClaims))
                .isInstanceOf(StatusServiceException.class)
                .hasMessageContaining("Pairing already initialized");

        verify(enrichDeviceDataStore, never()).enrich(any());
        verify(pairingStateStatusStore, never()).setStatusOrThrow(any());
    }
}
