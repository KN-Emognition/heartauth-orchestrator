package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.mapper.CreatePairingMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePairingServiceImpl implements CreatePairingService {

    private final CreateFlowStore<CreatePairing> pairingStateCreateFlowStore;
    private final CreatePairingMapper pairingCreateMapper;
    private final JwtEncoder jwtEncoder;

    @Override
    public PairingCreateResponse create(PairingCreateRequest req) {
        UUID jti = UUID.randomUUID();
        long ttl = 200L; // TODO: make configurable

        var now = java.time.Instant.now();
        var claims = org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                .subject(req.getUserId().toString())
                .id(jti.toString())
                .issuer("hauth:orchestrator")
                .audience(java.util.List.of("hauth:pairing"))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ttl))
                .build();

        var headers = org.springframework.security.oauth2.jwt.JwsHeader
                .with(org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.ES256)
                .build();

        String token = jwtEncoder.encode(
                org.springframework.security.oauth2.jwt.JwtEncoderParameters.from(headers, claims)
        ).getTokenValue();

        pairingStateCreateFlowStore.create(pairingCreateMapper.toCreatePairing(req, jti, 120L));
        return new PairingCreateResponse(jti, token);
    }
}

