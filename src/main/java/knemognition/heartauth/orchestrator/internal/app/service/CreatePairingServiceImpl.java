package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreatePairingMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.internal.config.pairing.InternalPairingProperties;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(InternalPairingProperties.class)
public class CreatePairingServiceImpl implements CreatePairingService {

    private final CreateFlowStore<CreatePairing> pairingStateCreateFlowStore;
    private final CreatePairingMapper pairingCreateMapper;
    private final JwtEncoder jwtEncoder;
    private final InternalPairingProperties internalPairingProperties;


    @Override
    public PairingCreateResponse create(PairingCreateRequest req) {
        UUID jti = UUID.randomUUID();
        Instant now = Instant.now();

        Integer effectiveTtl = clampOrDefault(
                req.getTtlSeconds(),
                internalPairingProperties.getMinTtl(),
                internalPairingProperties.getMaxTtl(),
                internalPairingProperties.getDefaultTtl()
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(req.getUserId().toString())
                .id(jti.toString())
                .issuer(internalPairingProperties.getIssuer())
                .audience(internalPairingProperties.getAudience())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(effectiveTtl))
                .build();

        JwsHeader headers = JwsHeader
                .with(SignatureAlgorithm.ES256)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
        pairingStateCreateFlowStore.create(pairingCreateMapper.toCreatePairing(req, jti, effectiveTtl));

        return new PairingCreateResponse(jti, token);
    }
}

