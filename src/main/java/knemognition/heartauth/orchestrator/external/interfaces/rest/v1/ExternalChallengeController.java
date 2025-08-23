package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.external.api.ChallengeApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.ChallengeStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@PreAuthorize("permitAll()")
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
        return ResponseEntity.ok(externalChallengeService.complete(id, request, dPoP));
    }
}
