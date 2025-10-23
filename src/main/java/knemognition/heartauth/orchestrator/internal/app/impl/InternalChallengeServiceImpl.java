package knemognition.heartauth.orchestrator.internal.app.impl;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.mapper.InternalChallengeMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
import knemognition.heartauth.orchestrator.internal.app.ports.in.KeyCreatorService;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.internal.app.ports.out.PushSender;
import knemognition.heartauth.orchestrator.internal.config.challenge.InternalChallengeProperties;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreateChallengeRequestDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreateChallengeResponseDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.NonceService;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.Clamp.clampOrDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(InternalChallengeProperties.class)
public class InternalChallengeServiceImpl implements InternalChallengeService {

    // utils
    private final InternalChallengeProperties internalChallengeProperties;
    private final NonceService nonceService;
    private final KeyCreatorService keyCreatorService;

    private final InternalChallengeMapper internalChallengeMapper;
    // sending
    private final PushSender pushSender;
    // persistence
    private final InternalChallengeStore internalChallengeStore;
    private final InternalMainStore internalMainStore;
    private final GetFlowStore<ChallengeState> challengeStateGetFlowStore;
    private final Environment env;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateChallengeResponseDto createChallenge(CreateChallengeRequestDto req, UUID tenantId) {

        IdentifiableUser user = IdentifiableUser.builder()
                .userId(req.getUserId())
                .tenantId(tenantId)
                .build();


        boolean exists = internalMainStore.checkIfUserExists(user);
        if (!exists) {
            throw new IllegalStateException("User with ID " + req.getUserId() + " doesnt exist.");
        }

        List<Device> deviceCredentials = internalMainStore.findDevices(user);
        log.info("Fetched fcmTokens for user {}", req.getUserId());
        if (deviceCredentials.isEmpty()) {
            log.info("No active devices for user {}", req.getUserId());
            throw new NoActiveDeviceException("No active device");
        }

        Integer effectiveTtl = clampOrDefault(req.getTtlSeconds(), internalChallengeProperties.getMinTtl(),
                internalChallengeProperties.getMaxTtl(), internalChallengeProperties.getDefaultTtl());

        String nonceB64 = nonceService.createNonce(internalChallengeProperties.getNonceLength());
        if (isE2eProfile()) {
            nonceB64 = user.getUserId()
                    .toString();
        }
        KeyPair keyPair = keyCreatorService.createEphemeralKeyPair();


        CreateChallenge to = internalChallengeMapper.toCreateChallenge(tenantId, req, nonceB64, effectiveTtl,
                keyPair.getPrivate(), deviceCredentials.getFirst()
                        .getPublicKey());
        CreatedFlowResult result = internalChallengeStore.createChallenge(to);
        log.info("Stored challenge in cache {} for user {}", result.getId(), req.getUserId());


        sendToDevices(deviceCredentials, nonceB64, result, keyPair.getPublic());
        return internalChallengeMapper.toCreateChallengeResponse(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponseDto getChallengeStatus(UUID id, UUID tenantId) {
        Optional<ChallengeState> state = challengeStateGetFlowStore.getFlow(id);
        log.info("Queried state for {}", id);

        if (state.isEmpty() || !tenantId.equals(state.get()
                .getTenantId())) {
            log.info("Tenant can't access this state {}", id);
            return internalChallengeMapper.notFoundStatus();
        }

        return internalChallengeMapper.toStatusResponseDto(state.get());
    }

    @Override
    public void completeChallengeWithPrediction(UUID correlationId, PredictResponseDto to) {
        ChallengeState state = internalChallengeStore.getChallengeStateByCorrelationId(correlationId);
        PredictResponse prediction = internalChallengeMapper.toPredictResponse(to);
        if (!(state.getStatus() == FlowStatus.PENDING)) {
            log.info("Prediction received for non-pending challenge {}, ignoring", state.getId());
            return;
        }
        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(state.getId());
        if (prediction.getPrediction() == true) {
            log.info("Prediction approved for challenge {}", state.getId());
            internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.APPROVED)
                    .reason(FlowStatusReason.FLOW_COMPLETED_SUCCESSFULLY_WITH_AUTHENTICATION)
                    .build());
        } else {
            log.info("Prediction rejected for challenge {}", state.getId());
            internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED)
                    .reason(FlowStatusReason.FLOW_DENIED_WITH_AUTHENTICATION_FAILURE)
                    .build());
        }
    }


    private void sendToDevices(List<Device> deviceCredentials, String nonceB64, CreatedFlowResult result, PublicKey publicKey) {
        ChallengePushMessage to = internalChallengeMapper.toChallengePushMessage(result, nonceB64, publicKey);

        for (Device item : deviceCredentials) {
            pushSender.sendData(item.getFcmToken(), to);
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

