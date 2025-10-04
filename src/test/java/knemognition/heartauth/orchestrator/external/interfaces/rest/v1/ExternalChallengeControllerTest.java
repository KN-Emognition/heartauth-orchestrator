package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import test.config.HeartauthUnitTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExternalChallengeControllerTest extends HeartauthUnitTest {

    @Mock
    private ExternalChallengeService completeChallengeService;

    @InjectMocks
    private ExternalChallengeController controller;

    @Test
    void externalChallengeComplete_returnsNoContentAndDelegates() {
        // given
        UUID id = UUID.randomUUID();
        ChallengeCompleteRequest req = new ChallengeCompleteRequest();
        // populate req fields if your model has required ones

        // when
        ResponseEntity<Void> resp = controller.externalChallengeComplete(id, req);

        // then
        assertThat(resp.getStatusCode().value()).isEqualTo(204);
        assertThat(resp.getBody()).isNull();
        verify(completeChallengeService, times(1)).completeChallenge(id, req);
        verifyNoMoreInteractions(completeChallengeService);
    }
}
