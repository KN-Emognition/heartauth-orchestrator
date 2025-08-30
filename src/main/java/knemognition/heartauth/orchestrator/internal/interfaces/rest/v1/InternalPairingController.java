package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.internal.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import knemognition.heartauth.orchestrator.internal.api.PairingApi;

import java.util.UUID;

@PreAuthorize("hasAuthority('keycloak')")
@Slf4j
@RestController
public class InternalPairingController implements PairingApi {


    private final StatusService pairingStatusService;

    public InternalPairingController(@Qualifier("pairingStatusServiceImpl") StatusService pairingStatusService) {
        this.pairingStatusService = pairingStatusService;
    }

    @Override
    public ResponseEntity<StatusResponse> internalPairingStatus(
            UUID jti,
            String xKCSession
    ) {
        log.info("Received status request for jti: {}", jti);
        return ResponseEntity.ok()
//                .cacheControl(CacheControl.noStore().mustRevalidate().cachePrivate().sMaxAge(0, TimeUnit.SECONDS))
                .body(pairingStatusService.status(jti));
    }
}
