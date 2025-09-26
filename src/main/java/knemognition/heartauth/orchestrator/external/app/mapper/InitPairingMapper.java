package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InitPairingMapper {

    EnrichDeviceData toEnrichDeviceData(PairingInitRequest req, String nonceB64, UUID jti);
}
