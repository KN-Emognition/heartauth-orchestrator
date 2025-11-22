package knemognition.heartauth.orchestrator.api.rest.v1;

import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.api.ChallengeApi;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.api.PairingApi;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.api.WellKnownApi;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.*;
import knemognition.heartauth.orchestrator.challenges.api.ChallengesModule;
import knemognition.heartauth.orchestrator.pairings.api.PairingsModule;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@PreAuthorize("permitAll()")
@Slf4j
@RestController
@RequiredArgsConstructor
@Profile(SpringProfiles.MOBILE)
public class MobileController implements ChallengeApi, PairingApi, WellKnownApi {

    private final ChallengesModule challengesModule;
    private final PairingsModule pairingsModule;
    private final DtoMapper dtoMapper;
    private final ECKey publicJwk;

    @Override
    public ResponseEntity<Void> completeChallenge(UUID id, CompleteChallengeRequestDto request) {
        log.info("[MOBILE-CONTROLLER] Received challenge completion request for id {}", id);
        boolean ok = challengesModule.complete(dtoMapper.toCmd(request, id));
        return ok ? ResponseEntity.noContent()
                .build() : ResponseEntity.badRequest()
                .build();
    }

    @Override
    public ResponseEntity<Void> completePairing(CompletePairingRequestDto pairingConfirmRequest) {
        log.info("[MOBILE-CONTROLLER] Received pairing confirmation request");
        var cmd = dtoMapper.toCmd(pairingConfirmRequest);
        pairingsModule.complete(cmd);
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<InitPairingResponseDto> initPairing(InitPairingRequestDto pairingInitRequest) {
        log.info("[MOBILE-CONTROLLER] Received pairing initialization request for device {}",
                pairingInitRequest.getDeviceId());
        var cmd = dtoMapper.toCmd(pairingInitRequest);
        return ResponseEntity.ok(dtoMapper.toDto(pairingsModule.init(cmd)));
    }

    @Override
    public ResponseEntity<JwkSetDto> getWellKnown() {
        ECJwkDto ec = new ECJwkDto();
        ec.setKty(ECJwkDto.KtyEnum.EC);
        ec.setCrv(ECJwkDto.CrvEnum.P_256);
        ec.setX(publicJwk.getX()
                .toString());
        ec.setY(publicJwk.getY()
                .toString());
        ec.setKid(publicJwk.getKeyID());
        ec.setUse(ECJwkDto.UseEnum.SIG);
        ec.setAlg("ES256");

        ec.setX5u(null);
        ec.setX5c(null);
        ec.setX5t(null);
        ec.setX5tHashS256(null);

        JwkSetDto body = new JwkSetDto();
        body.setKeys(List.of(ec));

        return ResponseEntity.ok(body);
    }
}
