package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreateChallengeMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FindFcmTokensStore;
import knemognition.heartauth.orchestrator.internal.config.challenge.InternalChallengeProperties;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreateChallengeService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FirebaseSender;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;


import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.withinOrDefault;
import static knemognition.heartauth.orchestrator.shared.utils.NonceUtils.createNonce;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(InternalChallengeProperties.class)
public class CreateChallengeServiceImpl implements CreateChallengeService {

    private final InternalChallengeProperties internalChallengeProperties;
    private final SecureRandom secureRandom;
    private final FindFcmTokensStore deviceCredentialStore;
    private final FirebaseSender firebaseSender;
    private final CreateChallengeMapper createChallengeMapper;
    private final CreateFlowStore<CreateChallenge> createChallengeCreateFlowStore;

    @Override
    public ChallengeCreateResponse create(ChallengeCreateRequest req) {
        List<String> fcmTokens = deviceCredentialStore.getActiveFcmTokens(req.getUserId());
        log.info("Fetched fcmTokens for user {}", req.getUserId());
        if (fcmTokens.isEmpty()) {
            log.info("No active devices for user {}", req.getUserId());
            throw new NoActiveDeviceException("No active device");
        }

        Integer effectiveTtl = withinOrDefault(
                req.getTtlSeconds(),
                internalChallengeProperties.getMinTtl(),
                internalChallengeProperties.getMaxTtl(),
                internalChallengeProperties.getDefaultTtl()
        );
        String nonceB64 = createNonce(secureRandom, internalChallengeProperties.getNonceLength());

        CreateChallenge to = createChallengeMapper.toCreateChallenge(req, nonceB64, effectiveTtl);
        CreatedFlowResult result = createChallengeCreateFlowStore.create(to);
        log.info("Stored challenge in cache {} for user {}", result.getId(), req.getUserId());


        sendToDevices(fcmTokens, nonceB64, result);
        return createChallengeMapper.toResponse(result);
    }


    private void sendToDevices(List<String> fcmTokens, String nonceB64, CreatedFlowResult result) {
        MessageData to = createChallengeMapper.toMessageData(result, nonceB64);

        for (String token : fcmTokens) {
            firebaseSender.sendData(token, to, Duration.ofSeconds(result.getTtl()));
        }
    }
}

