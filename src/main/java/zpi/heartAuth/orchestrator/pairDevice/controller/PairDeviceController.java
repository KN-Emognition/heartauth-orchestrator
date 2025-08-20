package zpi.heartAuth.orchestrator.pairDevice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import zpi.heartAuth.orchestrator.api.PairingApi;
import zpi.heartAuth.orchestrator.model.*;

import java.util.UUID;

@RestController
public class PairDeviceController implements PairingApi {

    @PreAuthorize("hasAuthority('keycloak')")
    @Override
    public ResponseEntity<PairingInitResponse> pairExchangeInitPost(PairingInitRequest pairingInitRequest) {
        return ResponseEntity.ok(new PairingInitResponse());
    }

    @PreAuthorize("hasAuthority('keycloak')")
    @Override
    public ResponseEntity<PairingStatusResponse> pairStatusJtiGet(UUID jti, String xKCSession) {
        return ResponseEntity.ok(new PairingStatusResponse());
    }

    public ResponseEntity<PairingConfirmResponse> pairExchangeConfirmPost(PairingConfirmRequest pairingConfirmRequest) {
        return ResponseEntity.ok(new PairingConfirmResponse());
    }
}
