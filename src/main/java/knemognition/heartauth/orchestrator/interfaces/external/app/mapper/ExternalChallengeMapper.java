package knemognition.heartauth.orchestrator.interfaces.external.app.mapper;

import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ExternalChallengeMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.userPublicKey")
    ValidateNonceCmd toValidateNonceCmd(CompleteChallengeRequestDto req, ChallengeState state);

    IdentifiableUser toIdentifiableUser(ChallengeState state);

    IdentifiableUserCmd toIdentifiableUserCmd(ChallengeState state);
}
