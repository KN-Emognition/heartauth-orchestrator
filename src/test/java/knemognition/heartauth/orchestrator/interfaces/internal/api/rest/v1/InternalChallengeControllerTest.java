//package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;
//
//import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
//import knemognition.heartauth.orchestrator.internal.app.impl.ChallengeStatusServiceImpl;
//import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
//import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
//import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.ResponseEntity;
//import test.config.HeartauthUnitTest;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class InternalChallengeControllerTest extends HeartauthUnitTest {
//
//    @Mock
//    private InternalChallengeService createChallengeService;
//
//    @Mock
//    private ChallengeStatusServiceImpl challengeStatusService;
//
//    @InjectMocks
//    private InternalChallengeController controller;
//
//    @Test
//    void internalChallengeCreate_returnsCreatedWithBody() {
//        // given
//        ChallengeCreateRequest req = new ChallengeCreateRequest();
//        // populate required fields if your model has them, e.g. req.setUserId(UUID.randomUUID());
//        ChallengeCreateResponse expected = new ChallengeCreateResponse();
//        when(createChallengeService.createChallenge(req)).thenReturn(expected);
//
//        // when
//        ResponseEntity<ChallengeCreateResponse> resp = controller.internalChallengeCreate(req);
//
//        // then
//        assertThat(resp.getStatusCode().value()).isEqualTo(201);
//        assertThat(resp.getBody()).isSameAs(expected);
//
//        verify(createChallengeService, times(1)).createChallenge(req);
//        verifyNoMoreInteractions(createChallengeService, challengeStatusService);
//    }
//
//    @Test
//    void internalChallengeStatus_returnsOkWithBody() {
//        // given
//        UUID id = UUID.randomUUID();
//        String xKCSession = "dummy-session";
//        StatusResponse expected = new StatusResponse();
//        when(challengeStatusService.status(id)).thenReturn(expected);
//
//        // when
//        ResponseEntity<StatusResponse> resp = controller.internalChallengeStatus(id, xKCSession);
//
//        // then
//        assertThat(resp.getStatusCode().value()).isEqualTo(200);
//        assertThat(resp.getBody()).isSameAs(expected);
//
//        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
//        verify(challengeStatusService, times(1)).status(captor.capture());
//        assertThat(captor.getValue()).isEqualTo(id);
//        verifyNoMoreInteractions(challengeStatusService, createChallengeService);
//    }
//}
