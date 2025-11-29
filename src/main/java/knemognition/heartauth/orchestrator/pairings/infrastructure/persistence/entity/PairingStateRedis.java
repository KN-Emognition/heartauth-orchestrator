package knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.entity;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.users.api.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("pairing_flow")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PairingStateRedis implements Serializable {
    @Id
    private UUID id;

    @Indexed
    private UUID tenantId;
    @Indexed
    private UUID userId;

    private FlowStatus status;
    private String reason;

    private String username;
    private String deviceId;
    private String displayName;
    private String publicKey;
    private String fcmToken;
    private Platform platform;
    private String osVersion;
    private String model;
    private String nonceB64;


    private Long exp;
    private Long createdAt;
    @TimeToLive
    private Long ttlSeconds;
}
