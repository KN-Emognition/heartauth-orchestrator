package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.internal.api.PairingApi;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalPairingService;
import knemognition.heartauth.orchestrator.internal.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.internal.model.CreatePairingResponseDto;
import knemognition.heartauth.orchestrator.internal.model.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.config.mdc.HeaderNames.ATTR_TENANT_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('KEYCLOAK')")
public class InternalPairingController implements PairingApi {

    private final InternalPairingService internalPairingService;
    private final HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<StatusResponseDto> getPairingStatus(UUID jti) {
        log.info("Received status request for jti: {}", jti);
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);
        log.info("tenantId: {}", tenantId);
        return ResponseEntity.ok()
                .body(internalPairingService.getPairingStatus(jti, tenantId));
    }

    @Override
    public ResponseEntity<CreatePairingResponseDto> createPairing(CreatePairingRequestDto req) {
        log.info("Received status request for token issue");
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);
        log.info("tenantId: {}", tenantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(internalPairingService.createPairing(req, tenantId));
    }
}
