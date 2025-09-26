package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.external.api.ChallengeApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompleteChallengeService;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@PreAuthorize("permitAll()")
@Slf4j
@RequiredArgsConstructor
public class ExternalChallengeController implements ChallengeApi {

    private final CompleteChallengeService completeChallengeService;

    @Override
    public ResponseEntity<Void> externalChallengeComplete(UUID id, ChallengeCompleteRequest request) {
        log.info("Received challenge completion request for id {}", id);
        completeChallengeService.complete(id, request);
        return ResponseEntity.noContent().build();
    }
}
