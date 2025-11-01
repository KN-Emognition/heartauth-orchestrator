package knemognition.heartauth.orchestrator.pairings.app.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.pairings.api.CompletePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.domain.StatusChange;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefTokenClaims;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.SaveUserDeviceCmd;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompletePairingHandler {
    private final SecurityModule securityModule;
    private final UserModule userModule;
    private final EcgModule ecgModule;
    private final PairingStore pairingStore;
    private final PairingsMapper pairingsMapper;


    @SneakyThrows
    @Transactional
    public void handle(CompletePairingCmd cmd) {
        var qrCodeClaims = securityModule.getQrClaims();
        UUID jti = qrCodeClaims.getJti();

        var state = pairingStore.getFlow(jti)
                .orElseThrow(() -> new NoPairingException("pairing_not_found_or_expired"));


        ValidateNonceCmd validateNonce = pairingsMapper.toCmd(cmd, state);
        securityModule.validateNonce(validateNonce);
        log.info("Nonce has been successfully validated");


        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(jti);
        if (!qrCodeClaims.getTenantId()
                .equals(state.getTenantId())) {
            throw new NoPairingException("No pairing for given tenant");
        }
        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoPairingException("pairing_replayed");
        }
        if (state.getStatus() != FlowStatus.PENDING) {
            throw new NoPairingException("Pairing status is not in pending");
        }

        DecryptJweCmd<EcgRefTokenClaims> toDecryptJwe = DecryptJweCmd.<EcgRefTokenClaims>builder()
                .jwe(cmd.getDataToken())
                .senderPublicKey(validateNonce.getPub())
                .targetType(new TypeReference<>() {
                })
                .build();

        EcgRefTokenClaims ecgRefToken = securityModule.decryptJwe(toDecryptJwe);
        log.info("JWT has been successfully verified");

        savePairingArtifacts(pairingsMapper.toCmd(state),
                ecgRefToken.getRefEcg(), pairingsMapper.toDevice(state));
        log.info("Saved artifacts to main store");

        pairingStore.setStatusOrThrow(statusChangeBuilder.status(FlowStatus.APPROVED)
                .reason(FlowStatusReason.FLOW_COMPLETED_SUCCESSFULLY)
                .build());
        log.info("Changed cache pairing status to Approved.");
    }

    private void savePairingArtifacts(IdentifiableUserCmd user, List<List<Float>> refEcg, DeviceCreate device) {
        var createdUser = userModule.saveUserDevice(SaveUserDeviceCmd.builder()
                .user(user)
                .device(device)
                .build());
        ecgModule.saveReferenceData(SaveReferenceDataCmd.builder()
                .userId(createdUser.getId())
                .refEcg(refEcg)
                .build());
    }
}
