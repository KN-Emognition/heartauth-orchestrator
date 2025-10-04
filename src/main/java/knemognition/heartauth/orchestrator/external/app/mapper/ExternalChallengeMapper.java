package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.shared.app.mapper.PemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.security.interfaces.ECPublicKey;


@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface ExternalChallengeMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.userPublicKey", qualifiedByName = "pemToEcPublicKey")
    ValidateNonce toValidateNonce(CompleteChallengeRequestDto req, ChallengeState state);

    IdentifiableUser toIdentifiableUser(ChallengeState state);

    @Mapping(target = "recipientPrivateKey", source = "privateKeyPem", qualifiedByName = "pemToEcPrivateKey")
    DecryptJwe toDecryptJwe(String jwe, String privateKeyPem, ECPublicKey senderPublicKey);
}
