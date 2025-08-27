package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import knemognition.heartauth.orchestrator.internal.api.PairingApi;
import knemognition.heartauth.orchestrator.internal.model.PairingStatusResponse;

import java.util.UUID;

@PreAuthorize("hasAuthority('keycloak')")
@Slf4j
@RestController
public class InternalPairingController implements PairingApi {

    @Override
    public ResponseEntity<PairingStatusResponse> internalPairingStatus(
            UUID jti,
            String xKCSession
    ) {
        log.info("Pairing status requested for jti: {}", jti);
        return ResponseEntity.ok(new PairingStatusResponse());
    }
}
