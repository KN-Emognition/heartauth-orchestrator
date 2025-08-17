package zpi.heartAuth.orchestrator.pairDevice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import zpi.heartAuth.orchestrator.api.PairingApi;
import zpi.heartAuth.orchestrator.model.PairingInitResponse;

@RestController
public class PairDeviceController implements PairingApi {

    @PreAuthorize("hasAuthority('keycloak')")
    @Override
    public ResponseEntity<PairingInitResponse> pairExchangeInitPost(
            @org.springframework.web.bind.annotation.RequestBody
            zpi.heartAuth.orchestrator.model.PairingInitRequest pairingInitRequest) {
        return ResponseEntity.ok(new PairingInitResponse());
    }
}
