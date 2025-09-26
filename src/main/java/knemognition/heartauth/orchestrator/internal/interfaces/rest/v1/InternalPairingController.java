package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.internal.api.PairingApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.app.service.PairingStatusServiceImpl;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('keycloak')")
public class InternalPairingController implements PairingApi {

    private final PairingStatusServiceImpl pairingStateStatusService;
    private final CreatePairingService createPairingService;


    @Override
    public ResponseEntity<StatusResponse> internalPairingStatus(UUID jti, String xKCSession) {
        log.info("Received status request for jti: {}", jti);
        return ResponseEntity.ok().body(pairingStateStatusService.status(jti));
    }

    @Override
    public ResponseEntity<PairingCreateResponse> internalPairingCreate(PairingCreateRequest req) {
        log.info("Received status request for token issue");
        return ResponseEntity.status(HttpStatus.CREATED).body(createPairingService.create(req));
    }
}
