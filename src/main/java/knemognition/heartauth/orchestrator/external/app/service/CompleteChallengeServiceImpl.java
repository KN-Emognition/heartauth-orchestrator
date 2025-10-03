package knemognition.heartauth.orchestrator.external.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;
import knemognition.heartauth.orchestrator.external.app.domain.EcgTestToken;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.mapper.CompleteChallengeMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.EcgTokenMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompleteChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.in.ValidateNonceService;
import knemognition.heartauth.orchestrator.external.app.ports.out.GetEcgRefTokenStore;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.ChallengeFailedException;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoChallengeException;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.modelapi.api.PredictionApi;
import knemognition.heartauth.orchestrator.modelapi.model.PredictRequest;
import knemognition.heartauth.orchestrator.modelapi.model.PredictResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.utils.EcgDataTokenDecryptor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteChallengeServiceImpl implements CompleteChallengeService {

    private final GetFlowStore<ChallengeState> challengeStateGetFlowStore;
    private final PredictionApi predictionApi;
    private final StatusStore<ChallengeState> statusStore;
    private final GetEcgRefTokenStore ecgRefTokenGetStore;
    private final ValidateNonceService validateNonceService;
    private final CompleteChallengeMapper completeChallengeMapper;
    private final PemMapper pemMapper;
    private final EcgTokenMapper ecgTokenMapper;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public void complete(UUID challengeId, ChallengeCompleteRequest req) {
        ChallengeState state = challengeStateGetFlowStore.getFlow(challengeId).orElseThrow(() -> new NoChallengeException("challenge_not_found_or_expired"));

        ValidateNonce validateNonce = completeChallengeMapper.toValidateNonce(req, state);
        validateNonceService.validate(validateNonce);
        log.info("Nonce has been successfully validated");

        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }

        JWTClaimsSet claims = EcgDataTokenDecryptor.decryptAndVerify(req.getDataToken(), pemMapper.privateMapAndValidate(state.getEphemeralPrivateKey()), validateNonce.getPub());
        log.info("JWT has been successfully verified");
        EcgTestToken ecgTestToken = ecgTokenMapper.ecgTestFromClaims(claims, objectMapper);
        log.info("Decrypted and verified EcgDataToken");

        Optional<List<EcgRefToken>> refTokensOpt = ecgRefTokenGetStore.getRefToken(state.getUserId());
        if (refTokensOpt.isEmpty() || refTokensOpt.get().isEmpty()) {
            statusStore.setStatus(StatusChange.builder().id(challengeId).status(FlowStatus.DENIED).build());
            log.info("Reference ECG data not found for user {}", state.getUserId());
            throw new ChallengeFailedException("No reference ECG data found");
        }
        log.info("Found reference data for user {}", state.getUserId());
        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder().id(challengeId);


        PredictResponse prediction;
        try {
            PredictRequest request = PredictRequest.builder().testEcg(ecgTestToken.getTestEcg()).refEcg(refTokensOpt.get().getFirst().getRefEcg()).build();
            prediction = predictionApi.predict(request);
            log.info("Called model for prediction.");
        } catch (Exception e) {
            log.info("model-api call failed for challenge {}", challengeId, e);
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
