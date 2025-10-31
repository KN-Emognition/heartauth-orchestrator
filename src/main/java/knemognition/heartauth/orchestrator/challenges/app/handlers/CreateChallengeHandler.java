package knemognition.heartauth.orchestrator.challenges.app.handlers;


import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallenge;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengeMapper;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
import knemognition.heartauth.orchestrator.internal.config.challenge.InternalChallengeProperties;
import knemognition.heartauth.orchestrator.security.api.SecurityApi;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import knemognition.heartauth.orchestrator.shared.FlowStatusReason;
import knemognition.heartauth.orchestrator.shared.SpringProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Service
@RequiredArgsConstructor
public class CreateChallengeHandler {

    private final ChallengeStateRepository repo;
    private final ChallengeMapper mapper;
    private final ApplicationEventPublisher events;
    private final SecurityApi securityApi;
    private final Environment env;
    private final InternalChallengeProperties internalChallengeProperties;

    private static final EnumSet<FlowStatus> TERMINAL = EnumSet.of(
            FlowStatus.APPROVED, FlowStatus.DENIED, FlowStatus.EXPIRED
    );

    public CreatedChallenge handle(CreateChallengeCmd cmd) {
        denyPreviousActive(cmd.getTenantId(), cmd.getUserId());

        Integer effectiveTtl = clampOrDefault(req.getTtlSeconds(), internalChallengeProperties.getMinTtl(),
                internalChallengeProperties.getMaxTtl(), internalChallengeProperties.getDefaultTtl());

        String nonceB64 = securityApi.createNonce(internalChallengeProperties.getNonceLength());
        if (isE2eProfile()) {
            nonceB64 = user.getUserId()
                    .toString();
        }
        KeyPair keyPair = securityApi.createEphemeralKeyPair();

        ChallengeStateRedis ent = mapper.fromCreate(cmd);
        ent.setId(UUID.randomUUID());
        ent.setCreatedAt(Instant.now()
                .getEpochSecond());
        ent.setReason(FlowStatusReason.FLOW_CREATED);
        ent.setStatus(FlowStatus.CREATED);
        repo.save(ent);

//        events.publishEvent(new events.ChallengeCreated(ent.getId(), ent.getModelApiTryId()));
        return CreatedChallenge.builder()
                .build();
    }


    private void denyPreviousActive(UUID tenantId, UUID userId) {
        List<ChallengeStateRedis> actives = repo.findAllByTenantIdAndUserIdOrderByCreatedAtDesc(tenantId, userId)
                .stream()
                .filter(e -> {
                    FlowStatus st = e.getStatus();
                    if (st == null || TERMINAL.contains(st)) return false;
                    Long exp = e.getExp();
                    long now = Instant.now()
                            .getEpochSecond();
                    return exp == null || exp > now;
                })
                .toList();

        for (ChallengeStateRedis e : actives) {
            e.setStatus(FlowStatus.DENIED);
            e.setReason("FLOW_DENIED_BY_RECREATING_FLOW");
            repo.save(e);
        }
    }


    private boolean isE2eProfile() {
        for (String profile : env.getActiveProfiles()) {
            if (SpringProfiles.E2E.equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
