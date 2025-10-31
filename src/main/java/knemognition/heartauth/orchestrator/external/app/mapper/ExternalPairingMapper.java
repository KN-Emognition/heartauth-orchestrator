package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import knemognition.heartauth.orchestrator.user.domain.Device;
import knemognition.heartauth.orchestrator.user.domain.EcgRefData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface ExternalPairingMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.publicKey",
            qualifiedByName = "pemToEcPublicKey")
    ValidateNonceCmd toValidateNonce(CompletePairingRequestDto req, PairingState state);

    EnrichDeviceData toEnrichDeviceData(InitPairingRequestDto req, String nonceB64, UUID jti);

    IdentifiableUserCmd toIdentifiableUser(PairingState state);

    EcgRefData toEcgRefData(EcgRefTokenClaims claims);

    Device toDevice(PairingState state);
}
