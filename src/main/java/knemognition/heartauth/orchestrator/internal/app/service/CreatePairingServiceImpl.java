package knemognition.heartauth.orchestrator.internal.app.service;

import io.jsonwebtoken.Jwts;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreatePairingMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePairingServiceImpl implements CreatePairingService {

    private final CreateFlowStore<CreatePairing> pairingStateCreateFlowStore;
    private final CreatePairingMapper pairingCreateMapper;
    private final ECPrivateKey ecPrivateKey;

    @Override
    public PairingCreateResponse create(PairingCreateRequest req) {
        UUID jti = UUID.randomUUID();
        long ttl = 200L;
        // TODO move ttl to request
        String token = Jwts.builder()
                .subject(req.getUserId().toString())
                .id(jti.toString())
                .issuer("hauth:orchestrator")
                .audience().add("hauth:pairing").and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl * 1000L))
                .signWith(ecPrivateKey, Jwts.SIG.ES256)
                .compact();
        CreatePairing to = pairingCreateMapper.toCreatePairing(req, jti, 120L);
        pairingStateCreateFlowStore.create(to);
        return new PairingCreateResponse(jti, token);
    }

}
