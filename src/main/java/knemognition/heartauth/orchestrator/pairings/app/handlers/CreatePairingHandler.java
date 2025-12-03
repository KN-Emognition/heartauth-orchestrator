package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.CreatePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.CreatedPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePairingHandler {

    private final UserModule userModule;
    private final JwtEncoder jwtEncoder;
    private final PairingsMapper mapper;
    private final PairingProperties properties;
    private final PairingStore store;

    public CreatedPairingRead createPairing(CreatePairingCmd cmd) {
        boolean exists = userModule.checkIfUserExists(mapper.toCmd(cmd));
        if (exists) {
            throw new NoPairingException("User with ID " + cmd.getUserId() + " already has benn paired.");
        }
        log.info("[PAIRING] User with given ID doesnt exit {}", cmd.getUserId());
        var effectiveTtl = clampOrDefault(cmd.getTtlSeconds(), properties.getMinTtl(), properties.getMaxTtl(),
                properties.getDefaultTtl());
        var now = Instant.now();
        var qrCodeClaims = mapper.toClaims(cmd, UUID.randomUUID(), now.plusSeconds(effectiveTtl)
                .getEpochSecond());
        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.ES256)
                        .build(), mapper.toClaimsSet(properties, now, qrCodeClaims)))
                .getTokenValue();
        log.info("[PAIRING] Encoded JWT for user {}", cmd.getUserId());
        store.createPairing(mapper.toDomain(qrCodeClaims, effectiveTtl.longValue()));
        log.info("[PAIRING] Created pairing for user {}", cmd.getUserId());
        return mapper.toRead(qrCodeClaims, jwt, effectiveTtl.longValue());
    }
}
