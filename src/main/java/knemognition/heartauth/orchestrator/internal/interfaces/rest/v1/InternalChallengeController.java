package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.internal.api.ChallengeApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
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

import static knemognition.heartauth.orchestrator.shared.config.mdc.HeaderNames.ATTR_TENANT_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('KEYCLOAK')")
public class InternalChallengeController implements ChallengeApi {

    private final InternalChallengeService internalChallengeService;
    private final HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<ChallengeCreateResponse> internalChallengeCreate(ChallengeCreateRequest request) {
        log.info("Received challenge create request for user {}", request.getUserId());
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(internalChallengeService.createChallenge(request, tenantId));
    }


    @Override
    public ResponseEntity<StatusResponse> internalChallengeStatus(UUID id, String xKCSession) {
        log.info("Received status request for id {}", id);
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);
        return ResponseEntity.ok()
                .body(internalChallengeService.getChallengeStatus(id, tenantId));
    }
}
