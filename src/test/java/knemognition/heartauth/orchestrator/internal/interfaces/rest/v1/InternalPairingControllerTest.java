package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.app.service.PairingStatusServiceImpl;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import test.config.HeartauthUnitTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InternalPairingControllerTest extends HeartauthUnitTest {

    @Mock
    private PairingStatusServiceImpl pairingStatusService;

    @Mock
    private CreatePairingService createPairingService;

    @InjectMocks
    private InternalPairingController controller;

    @Test
    void internalPairingStatus_returnsOkWithBody() {
        // given
        UUID jti = UUID.randomUUID();
        String xKCSession = "dummy-session";
        StatusResponse expected = new StatusResponse(); // populate if needed
        when(pairingStatusService.status(jti)).thenReturn(expected);

        // when
        ResponseEntity<StatusResponse> resp = controller.internalPairingStatus(jti, xKCSession);

        // then
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(expected);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(pairingStatusService, times(1)).status(captor.capture());
        assertThat(captor.getValue()).isEqualTo(jti);
        verifyNoMoreInteractions(pairingStatusService, createPairingService);
    }

    @Test
    void internalPairingCreate_returnsCreatedWithBody() {
        // given
        PairingCreateRequest req = new PairingCreateRequest(); // populate if needed
        PairingCreateResponse expected = new PairingCreateResponse(); // populate if needed
        when(createPairingService.create(req)).thenReturn(expected);

        // when
        ResponseEntity<PairingCreateResponse> resp = controller.internalPairingCreate(req);

        // then
        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        assertThat(resp.getBody()).isSameAs(expected);

        verify(createPairingService, times(1)).create(req);
        verifyNoMoreInteractions(createPairingService, pairingStatusService);
    }
}
