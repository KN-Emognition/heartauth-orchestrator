package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.internal.api.ChallengeApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreateChallengeService;
import knemognition.heartauth.orchestrator.internal.app.service.ChallengeStatusServiceImpl;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('keycloak')")
public class InternalChallengeController implements ChallengeApi {

    private final CreateChallengeService createChallengeService;
    private final ChallengeStatusServiceImpl challengeStatusService;


    @Override
    public ResponseEntity<ChallengeCreateResponse> internalChallengeCreate(ChallengeCreateRequest request) {
        log.info("Received challenge create request for user {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createChallengeService.create(request));
    }


    @Override
    public ResponseEntity<StatusResponse> internalChallengeStatus(UUID id, String xKCSession) {
        log.info("Received status request for id {}", id);
        return ResponseEntity.ok().body(challengeStatusService.status(id));
    }
}
