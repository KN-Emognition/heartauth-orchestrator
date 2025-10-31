//package knemognition.heartauth.orchestrator.external.app.mapper;
//
//import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
//import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
//import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
//import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
//import knemognition.heartauth.orchestrator.user.api.IdentifiableUser;
//import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.security.interfaces.ECPublicKey;
//
//
//@Mapper(componentModel = "spring", uses = {PemMapper.class})
//public interface ExternalChallengeMapper {
//
//    @Mapping(target = "nonce", source = "state.nonceB64")
//    @Mapping(target = "signature", source = "req.signature")
//    @Mapping(target = "pub", source = "state.userPublicKey", qualifiedByName = "pemToEcPublicKey")
//    ValidateNonceCmd toValidateNonce(CompleteChallengeRequestDto req, ChallengeState state);
//
//    IdentifiableUser toIdentifiableUser(ChallengeState state);
//
//    @Mapping(target = "recipientPrivateKey", source = "privateKeyPem", qualifiedByName = "pemToEcPrivateKey")
//    DecryptJweCmd toDecryptJwe(String jwe, String privateKeyPem, ECPublicKey senderPublicKey);
//}
