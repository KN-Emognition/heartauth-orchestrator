//package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;
//
//import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
//import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
//import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
//import knemognition.heartauth.orchestrator.external.config.qr.QrClaimsProvider;
//import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
//import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
//import knemognition.heartauth.orchestrator.external.model.PairingInitResponse;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.ResponseEntity;
//import test.config.HeartauthUnitTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class ExternalPairingControllerTest extends HeartauthUnitTest {
//
//    @Mock
//    private InitPairingService initPairingService;
//
//    @Mock
//    private CompletePairingService completePairingService;
//
//    @Mock
//    private QrClaimsProvider qrClaimsProvider;
//
//    @InjectMocks
//    private ExternalPairingController controller;
//
//    @Test
//    void externalPairingConfirm_returnsNoContentAndDelegates() {
//        // given
//        PairingConfirmRequest req = new PairingConfirmRequest();
//        // populate req required fields if any
//        QrClaims claims = QrClaims.builder()
//                .build(); // whatever QrClaimsProvider#get returns in your app
//        when(qrClaimsProvider.get()).thenReturn(claims);
//
//        // when
//        ResponseEntity<Void> resp = controller.externalPairingConfirm(req);
//
//        // then
//        assertThat(resp.getStatusCode()
//                .value()).isEqualTo(204);
//        assertThat(resp.getBody()).isNull();
//        verify(qrClaimsProvider, times(1)).get();
//        verify(completePairingService, times(1)).complete(req, claims);
//        verifyNoMoreInteractions(completePairingService, initPairingService, qrClaimsProvider);
//    }
//
//    @Test
//    void externalPairingInit_returnsOkWithBodyAndDelegates() {
//        // given
//        PairingInitRequest req = new PairingInitRequest();
//        PairingInitResponse expected = new PairingInitResponse();
//        QrClaims claims = new QrClaims();
//        when(qrClaimsProvider.get()).thenReturn(claims);
//        when(initPairingService.init(req, claims)).thenReturn(expected);
//
//        // when
//        ResponseEntity<PairingInitResponse> resp = controller.externalPairingInit(req);
//
//        // then
//        assertThat(resp.getStatusCode()
//                .value()).isEqualTo(200);
//        assertThat(resp.getBody()).isSameAs(expected);
//        verify(qrClaimsProvider, times(1)).get();
//        verify(initPairingService, times(1)).init(req, claims);
//        verifyNoMoreInteractions(initPairingService, completePairingService, qrClaimsProvider);
//    }
//}
