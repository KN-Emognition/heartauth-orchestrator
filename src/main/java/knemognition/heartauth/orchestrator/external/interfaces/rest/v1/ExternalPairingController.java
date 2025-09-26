package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.external.api.PairingApi;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.external.config.qr.QrClaimsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class ExternalPairingController implements PairingApi {

    private final InitPairingService initPairingService;
    private final CompletePairingService completePairingService;
    private final QrClaimsProvider qrClaimsProvider;

    @Override
    public ResponseEntity<Void> externalPairingConfirm(PairingConfirmRequest pairingConfirmRequest) {
        log.info("Received pairing confirmation request for device {}", pairingConfirmRequest.getDeviceId());
        completePairingService.complete(pairingConfirmRequest, qrClaimsProvider.get());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PairingInitResponse> externalPairingInit(PairingInitRequest pairingInitRequest) {
        log.info("Received pairing initialization request for device {}", pairingInitRequest.getDeviceId());
        return ResponseEntity.ok(initPairingService.init(pairingInitRequest, qrClaimsProvider.get()));
    }
}
