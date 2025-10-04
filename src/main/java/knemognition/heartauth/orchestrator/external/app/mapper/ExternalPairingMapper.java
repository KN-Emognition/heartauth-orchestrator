package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface ExternalPairingMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.publicKey",
            qualifiedByName = "pemToEcPublicKey")
    ValidateNonce toValidateNonce(CompletePairingRequestDto req, PairingState state);

    EnrichDeviceData toEnrichDeviceData(InitPairingRequestDto req, String nonceB64, UUID jti);

    IdentifiableUser toIdentifiableUser(PairingState state);

    EcgRefData toEcgRefData(EcgRefTokenClaims claims);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    Device toDevice(PairingState state);
}
