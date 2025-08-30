package knemognition.heartauth.orchestrator.external.app.mapper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DeviceCredentialCreateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "deviceId", source = "deviceId")
    @Mapping(target = "displayName", source = "displayName")
    @Mapping(target = "publicKeyPem", source = "publicKeyPem")
    @Mapping(target = "fcmToken", source = "fcmToken")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "osVersion", source = "osVersion")
    @Mapping(target = "model", source = "model")
    @Mapping(target = "attestation", expression = "java(toAttestationJson(state, mapper))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    DeviceCredential fromPairingState(PairingState state, @Context ObjectMapper mapper);

    default JsonNode toAttestationJson(PairingState s, @Context ObjectMapper mapper) {
        if (s.getAttestationType() == null && s.getAttestationVerdict() == null && s.getAttestationPayloadJson() == null) {
            return null;
        }
        ObjectNode root = mapper.createObjectNode();
        if (s.getAttestationType() != null) root.put("type", s.getAttestationType());
        if (s.getAttestationVerdict() != null) root.put("verdict", s.getAttestationVerdict());
        if (s.getAttestationPayloadJson() != null) {
            try {
                root.set("payload", mapper.readTree(s.getAttestationPayloadJson()));
            } catch (Exception e) {
                root.put("payloadRaw", s.getAttestationPayloadJson());
            }
        }
        return root;
    }
}
