package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.model.Attestation;
import knemognition.heartauth.orchestrator.external.model.DeviceInfo;
import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;

import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = FlowStatus.class)
public interface PairingConfirmMapper {

    @Mapping(target = "status", expression = "java(FlowStatus.APPROVED)")
    @Mapping(target = "credential", source = "saved")
    PairingConfirmResponse approved(DeviceCredential saved, @Context PairingState pairingState);

    @Mapping(target = "deviceId", source = "deviceId")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "publicKeyPem", source = "publicKeyPem")
    @Mapping(target = "fcmToken", source = "fcmToken")
    @Mapping(target = "createdAt", source = "createdAt")
    DeviceInfo toDeviceInfo(DeviceCredential saved, @Context PairingState pairingState);

    default Long map(Instant value) {
        return value != null ? value.getEpochSecond() : Instant.now().getEpochSecond();
    }

//    @AfterMapping
//    default void attachAttestation(@MappingTarget DeviceInfo out, @Context PairingState ps) {
//        if (ps != null && ps.getAttestationType() != null) {
//            Attestation a = new Attestation();
//            a.setType(Attestation.TypeEnum.fromValue(ps.getAttestationType()));
//            a.setVerdict(ps.getAttestationVerdict());
//            out.setAttestation(a);
//        }
//    }
}
