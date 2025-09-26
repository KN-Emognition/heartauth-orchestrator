package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model;


import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("challenge")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeStateRedis implements Serializable {
    @Id
    private UUID id;

    private FlowStatus status;

    private UUID userId;
    private String nonceB64;
    private Long exp;
    private Long createdAt;
    private String reason;
    private String privateKeyPem;
    private String userPublicKeyPem;
    @TimeToLive
    private Long ttlSeconds;
}
