package knemognition.heartauth.orchestrator.api.rest.v1;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.internal.api.ChallengeApi;
import knemognition.heartauth.orchestrator.api.rest.v1.internal.api.PairingApi;
import knemognition.heartauth.orchestrator.api.rest.v1.internal.model.*;
import knemognition.heartauth.orchestrator.challenges.api.ChallengesModule;
import knemognition.heartauth.orchestrator.challenges.api.GetChallengeStatusCmd;
import knemognition.heartauth.orchestrator.pairings.api.GetPairingStatusCmd;
import knemognition.heartauth.orchestrator.pairings.api.PairingsModule;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.constants.HeaderNames.ATTR_TENANT_ID;


@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority(T(knemognition.heartauth.orchestrator.shared.constants.Authorities).TENANT)")
@Profile(SpringProfiles.INTERNAL)
public class InternalController implements PairingApi, ChallengeApi {

    private final HttpServletRequest httpServletRequest;
    private final DtoMapper dtoMapper;
    private final PairingsModule pairingsModule;
    private final ChallengesModule challengesModule;

    @Override
    public ResponseEntity<CreateChallengeResponseDto> createChallenge(CreateChallengeRequestDto request) {
        log.info("[INTERNAL-CONTROLLER] Received challenge create request for user {}", request.getUserId());
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);
        var cmd = dtoMapper.toCmd(request, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dtoMapper.toDto(challengesModule.create(cmd)));
    }


    @Override
    public ResponseEntity<StatusResponseDto> getChallengeStatus(UUID id) {
        log.info("[INTERNAL-CONTROLLER] Received status request for id {}", id);
        UUID tenantId = (UUID) httpServletRequest.getAttribute(ATTR_TENANT_ID);
        var cmd = GetChallengeStatusCmd.builder()
                .challengeId(id)
                .tenantId(tenantId)
                .build();
        return ResponseEntity.ok()
                .body(dtoMapper.toDto(challengesModule.getStatus(cmd)));
    }

    @Override
    public ResponseEntity<StatusResponseDto> getPairingStatus(UUID jti) {
        log.info("[INTERNAL-CONTROLLER] Received status request for jti: {}", jti);
        UUID tenantId = (UUID) httpServletRequest.getAttribute(HeaderNames.ATTR_TENANT_ID);
        var cmd = GetPairingStatusCmd.builder()
                .challengeId(jti)
                .tenantId(tenantId)
                .build();
        return ResponseEntity.ok()
                .body(dtoMapper.toDto(pairingsModule.getStatus(cmd)));
    }

    @Override
    public ResponseEntity<CreatePairingResponseDto> createPairing(CreatePairingRequestDto req) {
        log.info("[INTERNAL-CONTROLLER] Received status request for token issue for user {}", req.getUserId());
        UUID tenantId = (UUID) httpServletRequest.getAttribute(HeaderNames.ATTR_TENANT_ID);
        var cmd = dtoMapper.toCmd(req, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dtoMapper.toDto(pairingsModule.create(cmd)));
    }
}
