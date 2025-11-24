package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallengeRead;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.config.ChallengeProperties;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.firebase.api.FirebaseModule;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ChallengeProperties.class)
public class CreateChallengeHandler {

    private final ChallengesMapper mapper;
    private final SecurityModule securityModule;
    private final UserModule userModule;
    private final ChallengeProperties challengeProperties;
    private final FirebaseModule pushSender;
    private final Environment env;
    private final ChallengeStore challengeStore;

    public CreatedChallengeRead handle(CreateChallengeCmd cmd) {

        IdentifiableUserCmd user = mapper.toCmd(cmd);


        boolean exists = userModule.checkIfUserExists(user);
        if (!exists) {
            throw new IllegalStateException("User with ID " + cmd.getUserId() + " doesnt exist.");
        }

        var deviceCredentials = userModule.getUserDevices(user);

        log.info("[CHALLENGE] Fetched fcmTokens for user {}", cmd.getUserId());
        if (deviceCredentials.isEmpty()) {
            log.info("[CHALLENGE] No active devices for user {}", cmd.getUserId());
            throw new NoActiveDeviceException("No active device");
        }

        var effectiveTtl = clampOrDefault(cmd.getTtlSeconds(), challengeProperties.getMinTtl(),
                challengeProperties.getMaxTtl(), challengeProperties.getDefaultTtl());

        var nonceB64 = securityModule.createNonce(challengeProperties.getNonceLength());
        if (isE2eProfile()) {
            nonceB64 = user.getUserId()
                    .toString();
        }
        var keyPair = securityModule.createEphemeralKeyPair();


        var to = mapper.toDomain(cmd, nonceB64, effectiveTtl, keyPair.getPrivate(), deviceCredentials.getFirst()
                .getPublicKey());
        var result = challengeStore.createChallenge(to);
        log.info("[CHALLENGE] Stored challenge in cache {} for user {}", result.getId(), cmd.getUserId());


        sendToDevices(deviceCredentials, nonceB64, result, keyPair.getPublic());
        return mapper.toRead(result);
    }

    private void sendToDevices(List<DeviceRead> deviceCredentials, String nonceB64, CreatedChallengeResult result, PublicKey publicKey) {
        var to = mapper.toChallengePushMessage(result, nonceB64, publicKey);
        for (DeviceRead item : deviceCredentials) {
            pushSender.sendChallengeMessage(item.getFcmToken(), to);
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
