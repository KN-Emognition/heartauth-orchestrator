package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import knemognition.heartauth.orchestrator.internal.api.PairingApi;
import knemognition.heartauth.orchestrator.internal.model.PairingStatusResponse;

import java.util.UUID;

@PreAuthorize("hasAuthority('keycloak')")
@RestController
public class PairingController implements PairingApi {

    @Override
    public ResponseEntity<PairingStatusResponse> internalPairingStatus(
            UUID jti,
            String xKCSession
    ) {
        return ResponseEntity.ok(new PairingStatusResponse());
    }
}
