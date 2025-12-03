package knemognition.heartauth.orchestrator.pairings.app.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.pairings.api.*;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.pairings.domain.CreatePairing;
import knemognition.heartauth.orchestrator.pairings.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Mapper(componentModel = "spring")
public abstract class PairingsMapper {
    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "cmd.signature")
    @Mapping(target = "pub", source = "state.publicKey")
    public abstract ValidateNonceCmd toCmd(CompletePairingCmd cmd, PairingState state);

    public abstract PairingStatusRead toRead(PairingState state);

    public abstract IdentifiableUserCmd toCmd(CreatePairingCmd src);

    public abstract QrCodeClaims toClaims(CreatePairingCmd src, UUID jti, Long exp);

    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "notBefore", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract JwtClaimsSet toClaimsSet(PairingProperties src, Instant issuedAt, QrCodeClaims claims);

    public abstract CreatePairing toDomain(QrCodeClaims src, Long ttlSeconds);

    public abstract CreatedPairingRead toRead(QrCodeClaims qrCode, String jwt, Long ttl);

    Consumer<Map<String, Object>> map(QrCodeClaims claims) {
        return map -> map.putAll(objectMapper.convertValue(claims, new TypeReference<Map<String, String>>() {
        }));
    }

    public abstract IdentifiableUserCmd toCmd(PairingState state);

    public abstract DeviceCreate toDevice(PairingState state);

    public abstract EnrichDeviceData toEnrichDeviceData(InitPairingCmd cmd, String nonceB64, UUID jti);
}
