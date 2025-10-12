package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model;


import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
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

@RedisHash("login_challenge_flow")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeStateRedis implements Serializable {
    @Id
    private UUID id;

    @Indexed
    private UUID userId;
    @Indexed
    private UUID tenantId;

    private FlowStatus status;
    private String reason;

    private String ephemeralPrivateKey;
    private String userPublicKey;
    private String nonceB64;

    private Long exp;
    private Long createdAt;
    @TimeToLive
    private Long ttlSeconds;
}
