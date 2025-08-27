package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("pairing")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PairingStateRedis implements Serializable {
    @Id private UUID id;

    private UUID userId;
    private String deviceId;
    private String displayName;
    private String publicKeyPem;
    private String fcmToken;
    private String platform;
    private String osVersion;
    private String model;

    private String attestationType;
    private String attestationVerdict;
    private String attestationPayloadJson;

    private FlowStatus status;
    private String reason;

    private String nonceB64;
    private Long exp;
    private Long createdAt;

    @TimeToLive
    private Long ttlSeconds;    
}
