package knemognition.heartauth.orchestrator.internal.app.impl;


import knemognition.heartauth.orchestrator.internal.app.mapper.InternalPairingMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalPairingService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalPairingStore;
import knemognition.heartauth.orchestrator.internal.config.pairing.InternalPairingProperties;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreatePairingResponseDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.QrCodeClaims;
import knemognition.heartauth.orchestrator.shared.app.mapper.RecordMapper;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
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
import java.util.Optional;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(InternalPairingProperties.class)
public class InternalPairingServiceImpl implements InternalPairingService {

    // utils
    private final JwtEncoder jwtEncoder;
    private final InternalPairingProperties internalPairingProperties;
    private final InternalPairingMapper internalPairingMapper;
    private final RecordMapper recordMapper;
    // persistence
    private final InternalPairingStore internalPairingStore;
    private final InternalMainStore internalMainStore;
    private final GetFlowStore<PairingState> pairingStateGetFlowStore;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreatePairingResponseDto createPairing(CreatePairingRequestDto req, UUID tenantId) {


        boolean exists = internalMainStore.checkIfUserExists(IdentifiableUser.builder()
                .userId(req.getUserId())
                .tenantId(tenantId)
                .build());
        if (exists) {
            throw new IllegalStateException("User with ID " + req.getUserId() + " already has a pairing.");
        }

        UUID jti = UUID.randomUUID();
        Instant now = Instant.now();

        Integer effectiveTtl = clampOrDefault(req.getTtlSeconds(), internalPairingProperties.getMinTtl(),
                internalPairingProperties.getMaxTtl(), internalPairingProperties.getDefaultTtl());

        QrCodeClaims qrCodeClaims = QrCodeClaims.builder()
                .jti(jti)
                .userId(req.getUserId())
                .tenantId(tenantId)
                .exp(now.plusSeconds(effectiveTtl)
                        .getEpochSecond())
                .build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(internalPairingProperties.getIssuer())
                .audience(internalPairingProperties.getAudience())
                .issuedAt(now)
                .claims(claimsMap -> claimsMap.putAll(recordMapper.convertObjectToMap(qrCodeClaims)))
                .build();
        JwsHeader headers = JwsHeader.with(SignatureAlgorithm.ES256)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
        log.info("Encoded JWT for user {}", req.getUserId());
        internalPairingStore.createPairing(internalPairingMapper.toCreatePairing(tenantId, req, jti, effectiveTtl));
        log.info("Created pairing for user {}", req.getUserId());

        return CreatePairingResponseDto.builder()
                .jti(jti)
                .jwt(token)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponseDto getPairingStatus(UUID id, UUID tenantId) {
        Optional<PairingState> state = pairingStateGetFlowStore.getFlow(id);
        log.info("Queried state for {}", id);

        if (state.isEmpty() || !tenantId.equals(state.get()
                .getTenantId())) {
            log.info("Tenant can't access this state {}", id);
            return internalPairingMapper.notFoundStatus();
        }

        return internalPairingMapper.toStatusResponseDto(state.get());
    }
}

