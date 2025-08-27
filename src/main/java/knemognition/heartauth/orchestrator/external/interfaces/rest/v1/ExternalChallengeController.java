package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.external.api.ChallengeApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.ChallengeStatusResponse;
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
    private final ExternalChallengeService externalChallengeService;
    @Override
    public ResponseEntity<ChallengeStatusResponse> externalChallengeComplete(
            UUID id,
            @Valid ChallengeCompleteRequest request,
            String dPoP

    ) {
        log.info("Received challenge completion request for id {}", id);
        return ResponseEntity.ok(externalChallengeService.complete(id, request, dPoP));
    }
}
