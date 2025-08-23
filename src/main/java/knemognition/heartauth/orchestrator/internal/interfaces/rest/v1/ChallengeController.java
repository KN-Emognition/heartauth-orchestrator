package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import knemognition.heartauth.orchestrator.internal.api.ChallengeApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.ChallengeQueryService;
import knemognition.heartauth.orchestrator.internal.app.ports.in.ChallengeService;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeStatusResponse;

import java.util.UUID;

@PreAuthorize("hasAuthority('keycloak')")
@RestController
@RequiredArgsConstructor
public class ChallengeController implements ChallengeApi {

    private final ChallengeService challengeService;
    private final ChallengeQueryService challengeQueryService;


    @Override
    public ResponseEntity<ChallengeCreateResponse> internalChallengeCreate(
            @Valid ChallengeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.createAndDispatch(request));
    }


    @Override
    public ResponseEntity<ChallengeStatusResponse> internalChallengeStatus(UUID id, String xKCSession) {
        var body = challengeQueryService.status(id);
        return ResponseEntity.ok()
//                .cacheControl(CacheControl.noStore().mustRevalidate().cachePrivate().sMaxAge(0, TimeUnit.SECONDS))
                .body(body);
    }
}
