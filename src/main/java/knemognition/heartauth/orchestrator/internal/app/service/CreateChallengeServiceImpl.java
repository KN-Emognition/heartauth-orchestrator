package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreateChallengeMapper;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreateChallengeService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FirebaseSender;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;


import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateChallengeServiceImpl implements CreateChallengeService {

    private static final int NONCE_BYTES = 32;

    private final SecureRandom rng = new SecureRandom();

    private final DeviceCredentialStore deviceCredentialStore;
    private final FirebaseSender firebaseSender;
    private final CreateChallengeMapper createChallengeMapper;
    private final CreateFlowStore<CreateChallenge> createChallengeCreateFlowStore;

    @Override
    public ChallengeCreateResponse create(ChallengeCreateRequest req) {
        final List<String> fcmTokens = deviceCredentialStore.getActiveFcmTokens(req.getUserId());
        log.info("Fetched fcmTokens for user {}", req.getUserId());
        if (fcmTokens.isEmpty()) {
            log.info("No active devices for user {}", req.getUserId());
            throw new NoActiveDeviceException();
        }


        final String nonceB64 = Base64.getEncoder().encodeToString(randomBytes(NONCE_BYTES));

        CreateChallenge to = createChallengeMapper.toCreateChallenge(req, nonceB64);
        CreatedFlowResult result = createChallengeCreateFlowStore.create(to);
        log.info("Stored challenge in cache {} for user {}", result.getId(), req.getUserId());


        sendToDevices(fcmTokens, nonceB64, result);
        return createChallengeMapper.toResponse(result);
    }

    private void sendToDevices(List<String> fcmTokens, String nonceB64, CreatedFlowResult result) {
        var data = Map.of(
                "type", "ECG_CHALLENGE",
                "challengeId", String.valueOf(result.getId()),
                "nonce", nonceB64,
                "exp", String.valueOf(result.getExp())
        );

        for (String token : fcmTokens) {
            try {
                firebaseSender.sendData(token, data, Duration.ofSeconds(result.getTtl()));
                log.info("Sent challenge {} to device {}", result.getId(), token);
            } catch (Exception e) {
                log.error("Failed to send challenge to device");
            }
        }
    }

    private byte[] randomBytes(int n) {
        byte[] b = new byte[n];
        rng.nextBytes(b);
        return b;
    }
}

