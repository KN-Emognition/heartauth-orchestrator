package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.DeviceDirectory;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FcmSender;
import knemognition.heartauth.orchestrator.internal.app.mapper.ChallengeMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.ChallengeStore;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;


import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalInternalChallengeServiceImpl implements InternalChallengeService {

    private static final int NONCE_BYTES = 32;
    private static final int DEFAULT_TTL = 120;
    private static final int MIN_TTL = 30;
    private static final int MAX_TTL = 300;

    private final SecureRandom rng = new SecureRandom();

    private final DeviceDirectory deviceDirectory;
    private final FcmSender fcmSender;
    private final ChallengeStore challengeStore;
    private final ChallengeMapper challengeMapper;

    @Override
    public ChallengeCreateResponse createAndDispatch(ChallengeCreateRequest req) {
        log.info("creating challenge for user {}", req.getUserId());
        final List<String> fcmTokens = deviceDirectory.getActiveFcmTokens(req.getUserId());
        if (fcmTokens.isEmpty()) {
            log.info("no active devices for user {}", req.getUserId());
            throw new NoActiveDeviceException();
        }
        final UUID challengeId = UUID.randomUUID();
        final String nonceB64 = Base64.getEncoder().encodeToString(randomBytes(NONCE_BYTES));

        final int ttl = clamp(req.getTtlSeconds() == null ? DEFAULT_TTL : req.getTtlSeconds(),
                MIN_TTL, MAX_TTL);
        final long now = Instant.now().getEpochSecond();
        final long exp = now + ttl;

        ChallengeState state = challengeMapper.toState(req, challengeId,  nonceB64, exp, now);
        log.info("storing challenge in cache {} for user {}", challengeId, req.getUserId());
        challengeStore.create(state, Duration.ofSeconds(ttl));

        var data = Map.of(
                "type", "ECG_CHALLENGE",
                "challengeId", challengeId.toString(),
                "nonce", nonceB64,
                "exp", String.valueOf(exp)
        );
        for (String token : fcmTokens) {
            try {
                log.info("sending challenge {} to device {}", challengeId, token);
                fcmSender.sendData(token, data, Duration.ofSeconds(ttl));
            } catch (Exception e) {
                log.warn("failed to send challenge to device", e);
            }
        }
        return challengeMapper.toResponse(challengeId);
    }

    private byte[] randomBytes(int n) {
        byte[] b = new byte[n];
        rng.nextBytes(b);
        return b;
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}

