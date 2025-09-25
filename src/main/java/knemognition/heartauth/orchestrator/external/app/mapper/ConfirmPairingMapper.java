package knemognition.heartauth.orchestrator.external.app.mapper;


import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface ConfirmPairingMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.publicKeyPem",
            qualifiedByName = "pemToEcPublicKey")
    ValidateNonce toValidateNonce(PairingConfirmRequest req, PairingState state);

}
