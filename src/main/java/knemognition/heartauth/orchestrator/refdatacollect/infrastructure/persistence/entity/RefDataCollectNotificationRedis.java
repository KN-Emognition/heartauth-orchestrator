package knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity;

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

@RedisHash("ref_data_collect_flow")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefDataCollectNotificationRedis implements Serializable {
    @Id
    private UUID id;
    private UUID processId;

    @Indexed
    private UUID userInternalId;


    private String publicKeyPem;
    private String nonceB64;
    private String ephemeralPrivateKeyPem;
    private Boolean isCompleted;

    private Long exp;
    private Long createdAt;
    @TimeToLive
    private Long ttlSeconds;
}
