package knemognition.heartauth.orchestrator.external.app.mapper;


import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = FlowStatus.class)
public interface PairingCreateMapper {

    @Mapping(target = "id", source = "jti")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "deviceId", source = "req.deviceId")
    @Mapping(target = "displayName", source = "req.displayName")
    @Mapping(target = "publicKeyPem", source = "req.publicKeyPem")
    @Mapping(target = "fcmToken", source = "req.fcmToken")
    @Mapping(target = "platform", expression = "java(req.getPlatform() != null ? req.getPlatform().name() : null)")
    @Mapping(target = "osVersion", source = "req.osVersion")
    @Mapping(target = "model", source = "req.model")
    @Mapping(target = "attestationType", expression = "java(req.getAttestation() != null && req.getAttestation().getType() != null ? req.getAttestation().getType().name() : null)")
    @Mapping(target = "attestationVerdict", expression = "java(req.getAttestation() != null ? req.getAttestation().getVerdict() : null)")
    @Mapping(target = "attestationPayloadJson", expression = "java(req.getAttestation() != null && req.getAttestation().getPayload() != null ? req.getAttestation().getPayload().toString() : null)")
    @Mapping(target = "status", expression = "java(FlowStatus.PENDING)")
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "nonceB64", source = "nonceB64")
    @Mapping(target = "exp", source = "expEpochSeconds")
    @Mapping(target = "createdAt", source = "createdAtEpochSeconds")
    @Mapping(target = "ttlSeconds", source = "ttlSeconds")
    PairingState toState(PairingInitRequest req,
                         UUID jti,
                         UUID userId,
                         String nonceB64,
                         Long expEpochSeconds,
                         Long createdAtEpochSeconds,
                         Long ttlSeconds);
}
