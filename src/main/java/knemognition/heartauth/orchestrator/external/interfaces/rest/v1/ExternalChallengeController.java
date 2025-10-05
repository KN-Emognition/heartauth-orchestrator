package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.api.ChallengeApi;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@PreAuthorize("permitAll()")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ExternalChallengeController implements ChallengeApi {

    private final ExternalChallengeService completeChallengeService;

    @Override
    public ResponseEntity<Void> completeChallenge(UUID id, CompleteChallengeRequestDto request) {
        log.info("Received challenge completion request for id {}", id);
        completeChallengeService.completeChallenge(id, request);
        return ResponseEntity.noContent().build();
    }
}
