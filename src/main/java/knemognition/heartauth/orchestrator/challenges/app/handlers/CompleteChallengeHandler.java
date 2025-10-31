package knemognition.heartauth.orchestrator.challenges.app.handlers;


import com.fasterxml.jackson.core.type.TypeReference;
import knemognition.heartauth.orchestrator.challenges.api.EcgChallengeSendCmd;
import knemognition.heartauth.orchestrator.challenges.app.exceptions.NoChallengeException;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengeMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ModelApiProducer;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityApi;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.shared.FlowStatusReason;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgTestTokenClaims;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteChallengeHandler {

    private final SecurityApi securityApi;
    private final ChallengeStateRepository repo;
    private final RefDataPort refData;
    private final ModelApiProducer modelApi;
    private final ChallengeMapper challengeMapper;
    private final ApplicationEventPublisher events;

    @SneakyThrows
    public void handle(EcgChallengeSendCmd cmd) {
        var ent = repo.findById(cmd.getChallengeId())
                .filter(e -> e.getExp() == null || e.getExp() > Instant.now()
                        .getEpochSecond())
                .orElseThrow(() -> new RuntimeException("challenge_not_found_or_expired"));
        var state = challengeMapper.toDomain(ent);


        if (ent.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }

        var validateNonce = ValidateNonceCmd.builder()
                .signature(cmd.getSignature())
                .nonce(state.getNonceB64())
                .pub(state.getUserPublicKey())
                .build();
        securityApi.validateNonce(validateNonce);

        var jwe = DecryptJweCmd.<EcgTestTokenClaims>builder()
                .jwe(cmd.getDataToken())
                .senderPublicKey(state.getUserPublicKey())
                .recipientPrivateKey(state.getEphemeralPrivateKey())
                .targetType(new TypeReference<EcgTestTokenClaims>() {
                })
                .build();
        var testToken = securityApi.decryptJwe(jwe);

        Optional<RefDataPort.EcgRefData> ref = refData.findRefData(new RefDataPort.IdentifiableUser(
                ent.getTenantId()
                        .toString(), ent.getUserId()
                .toString()
        ));

        if (ref.isEmpty()) {
            ent.setStatus(FlowStatus.DENIED);
            ent.setReason(FlowStatusReason.FLOW_DENIED_WITHOUT_AUTHENTICATION);
            repo.save(ent);
            events.publishEvent(new events.ChallengeDenied(ent.getId(), "NO_REF_DATA"));
            throw new RuntimeException("No reference ECG data found");
        }

        modelApi.predict(ent.getModelApiTryId(),
                new ModelApiPort.EcgPayload(token.testEcg(), ref.get()
                        .refEcg()));

        ent.setStatus(FlowStatus.PENDING);
        ent.setReason("FLOW_WAITING_FOR_MODEL");
        repo.save(ent);
        events.publishEvent(new events.ChallengePending(ent.getId()));
    }
}
