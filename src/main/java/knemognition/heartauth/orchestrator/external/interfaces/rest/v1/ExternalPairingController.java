package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.external.api.PairingApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.external.config.qr.QrClaimsProvider;
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
    private final QrClaimsProvider qrClaimsProvider;

    public ResponseEntity<Void> externalPairingConfirm(@Valid PairingConfirmRequest pairingConfirmRequest
    ) {
        log.info("Received pairing confirmation request for device {}", pairingConfirmRequest.getDeviceId());
        completePairingService.complete(pairingConfirmRequest, qrClaimsProvider.get());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<PairingInitResponse> externalPairingInit(
            @Valid PairingInitRequest pairingInitRequest
    ) {
        log.info("Received pairing initialization request for device {}", pairingInitRequest.getDeviceId());
        return ResponseEntity.ok(initPairingService.init(pairingInitRequest, qrClaimsProvider.get()));
    }
}
