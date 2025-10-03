package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface CompleteChallengeMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.userPublicKey", qualifiedByName = "pemToEcPublicKey")
    ValidateNonce toValidateNonce(ChallengeCompleteRequest req, ChallengeState state);

}
