package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.internal.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import knemognition.heartauth.orchestrator.internal.api.ChallengeApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreateChallengeService;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;

import java.util.UUID;

@PreAuthorize("hasAuthority('keycloak')")
@RestController
@Slf4j
public class InternalChallengeController implements ChallengeApi {

    private final CreateChallengeService createChallengeService;
    private final StatusService challengeStatusService;

    public InternalChallengeController(@Qualifier("challengeStatusServiceImpl") StatusService challengeStatusService,
                                       CreateChallengeService createChallengeService) {
        this.challengeStatusService = challengeStatusService;
        this.createChallengeService = createChallengeService;
    }

    @Override
    public ResponseEntity<ChallengeCreateResponse> internalChallengeCreate(
            @Valid ChallengeCreateRequest request) {
        log.info("Received challenge create request for user {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createChallengeService.createAndDispatch(request));
    }


    @Override
    public ResponseEntity<StatusResponse> internalChallengeStatus(UUID id, String xKCSession) {
        log.info("Received status request for id {}", id);
        return ResponseEntity.ok()
//                .cacheControl(CacheControl.noStore().mustRevalidate().cachePrivate().sMaxAge(0, TimeUnit.SECONDS))
                .body(challengeStatusService.status(id));
    }
}
