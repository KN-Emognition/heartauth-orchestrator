package knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1;

import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.api.PairingApi;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.interfaces.external.app.ports.in.ExternalPairingService;
import knemognition.heartauth.orchestrator.interfaces.external.config.qr.QrClaimsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExternalPairingController implements PairingApi {

    private final ExternalPairingService externalPairingService;
    private final QrClaimsProvider qrClaimsProvider;

    @Override
    public ResponseEntity<Void> completePairing(CompletePairingRequestDto pairingConfirmRequest) {
        log.info("Received pairing confirmation request");
        externalPairingService.completePairing(pairingConfirmRequest, qrClaimsProvider.get());
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<InitPairingResponseDto> initPairing(InitPairingRequestDto pairingInitRequest) {
        log.info("Received pairing initialization request for device {}", pairingInitRequest.getDeviceId());
        return ResponseEntity.ok(externalPairingService.initPairing(pairingInitRequest, qrClaimsProvider.get()));
    }
}
