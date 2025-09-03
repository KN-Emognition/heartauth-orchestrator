package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.mapper.PairingCreateMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.FlowStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitPairingServiceImpl implements InitPairingService {

    private final JwtServiceImpl jwtService;
    private final FlowStore<PairingState> pairingStore;
    private final PairingCreateMapper pairingCreateMapper;

    @Override
    public PairingInitResponse init(PairingInitRequest req, QrClaims claims) {
        Instant now = Instant.now();
        long ttlByJwt = Math.max(0, claims.exp().getEpochSecond() - now.getEpochSecond());
        long ttl = Math.min(ttlByJwt, 60);
        if (ttl <= 0) throw new IllegalArgumentException("pairing_token_expired");

        String nonceB64 = createNonce();

        long exp = now.plusSeconds(ttl).getEpochSecond();

        PairingState pairingState = pairingCreateMapper.toState(
                req,
                claims.jti(),
                claims.userId(),
                nonceB64,
                exp,
                now.getEpochSecond(),
                ttl
        );
        pairingStore.create(pairingState, Duration.ofSeconds(ttl));
        log.info("Stored pairing in cache {} for user {}", pairingState.getId(), pairingState.getUserId());


        return new PairingInitResponse(claims.jti(), nonceB64, pairingState.getExp());
    }

    private String createNonce() {
        byte[] nonce = new byte[32];
        new SecureRandom().nextBytes(nonce);
        String nonceB64 = Base64.getEncoder().encodeToString(nonce);
        return nonceB64;
    }
}
