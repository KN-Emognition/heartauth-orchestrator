package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import knemognition.heartauth.orchestrator.external.api.PairingApi;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.config.rest.security.PairingJwtInterceptor;
import knemognition.heartauth.orchestrator.external.model.*;
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
    private final HttpServletRequest request;

    public ResponseEntity<Void> externalPairingConfirm(@Valid PairingConfirmRequest pairingConfirmRequest
    ) {
        QrClaims claims = (QrClaims) request.getAttribute(
                PairingJwtInterceptor.REQ_ATTR_QR_CLAIMS);
        log.info("Received pairing confirmation request for device {}", pairingConfirmRequest.getDeviceId());
        completePairingService.complete(pairingConfirmRequest, claims);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<PairingInitResponse> externalPairingInit(
            @Valid PairingInitRequest pairingInitRequest
    ) {
        QrClaims claims = (QrClaims) request.getAttribute(
                PairingJwtInterceptor.REQ_ATTR_QR_CLAIMS);
        log.info("Received pairing initialization request for device {}", pairingInitRequest.getDeviceId());
        return ResponseEntity.ok(initPairingService.init(pairingInitRequest, claims));
    }
}
