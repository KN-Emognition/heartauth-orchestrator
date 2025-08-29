package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.external.api.PairingApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmResponse;
import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import knemognition.heartauth.orchestrator.external.model.PairingInitResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExternalPairingController implements PairingApi {

    private final InitPairingService initPairingService;
    private final CompletePairingService completePairingService;

    public ResponseEntity<PairingConfirmResponse> externalPairingConfirm(@Valid PairingConfirmRequest pairingConfirmRequest
    ) {
        log.info("Received pairing confirmation request for device {}", pairingConfirmRequest.getDeviceId());
        return ResponseEntity.ok(completePairingService.complete(pairingConfirmRequest));
    }

    public ResponseEntity<PairingInitResponse> externalPairingInit(
            @Valid PairingInitRequest pairingInitRequest
    ) {
        log.info("Received pairing initialization request for device {}", pairingInitRequest.getDeviceId());
        return ResponseEntity.ok(initPairingService.init(pairingInitRequest));
    }
}
