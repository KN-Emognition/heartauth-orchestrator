package knemognition.heartauth.orchestrator.external.app.service;

import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.mapper.CompleteChallengeMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompleteChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.in.ValidateNonceService;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.ChallengeFailedException;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoChallengeException;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.modelapi.api.PredictionApi;
import knemognition.heartauth.orchestrator.modelapi.model.In;
import knemognition.heartauth.orchestrator.modelapi.model.PredictResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import knemognition.heartauth.orchestrator.shared.app.ports.out.ChallengeStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.utils.JwtDecryptor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteChallengeServiceImpl implements CompleteChallengeService {

    private final ChallengeStore challengeStore;
    private final PredictionApi predictionApi;
    private final StatusStore<ChallengeState> statusStore;
    private final ValidateNonceService validateNonceService;
    private final CompleteChallengeMapper completeChallengeMapper;
    private final PemMapper pemMapper;

    @SneakyThrows
    @Override
    public void complete(UUID challengeId, ChallengeCompleteRequest req) {
        ChallengeState state = challengeStore.getChallengeState(challengeId).orElseThrow(() -> new NoChallengeException("challenge_not_found_or_expired"));

        ValidateNonce validateNonce = completeChallengeMapper.toValidateNonce(req, state);
        validateNonceService.validate(validateNonce);
        log.info("Nonce has been successfully validated");

        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }
//        if (state.getStatus() != FlowStatus.CREATED) {
//            throw new NoChallengeException("Challenge status is not in pending");
//        }

        log.info("PEM {}", state.getPrivateKeyPem());
        ECPrivateKey privateKey;
        try {
            privateKey = pemMapper.privateMapAndValidate(state.getPrivateKeyPem());
        } catch (Exception e) {
            log.error("Invalid private key PEM for challenge {}", challengeId, e);
            throw new ChallengeFailedException("Invalid private key PEM");
        }
        JWTClaimsSet data = JwtDecryptor.decryptAndVerify(
                req.getData(), privateKey,
                validateNonce.getPub()
        );
        log.info("JWT has been successfully verified");
        log.info(data.getClaimAsString("data"));

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder().id(challengeId);


        In in = new In().anything("anything");
        PredictResponse prediction;
        try {
            prediction = predictionApi.predict(in);
            log.info("Called model for prediction.");
        } catch (Exception e) {
            log.warn("model-api call failed for challenge {}", challengeId, e);
            statusStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED).build());
            throw new ChallengeFailedException("Can't access Model API");
        }

        boolean approved = prediction != null && prediction.getPrediction();

        if (!approved) {
            statusStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED).build());
            throw new ChallengeFailedException("ECG Validation failed");
        }
        statusStore.setStatus(statusChangeBuilder.status(FlowStatus.APPROVED).build());
    }
}
