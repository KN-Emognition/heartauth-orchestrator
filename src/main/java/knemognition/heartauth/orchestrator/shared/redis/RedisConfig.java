package knemognition.heartauth.orchestrator.shared.redis;

import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.UUID;

@Configuration
public class RedisConfig {

    @Bean("challengeStateRedisTemplate")
    public RedisTemplate<UUID, ChallengeState> challengeStateRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        var t = new RedisTemplate<UUID, ChallengeState>();
        t.setConnectionFactory(connectionFactory);

        var uuidKey = new GenericToStringSerializer<>(UUID.class);
        t.setKeySerializer(uuidKey);
        t.setHashKeySerializer(uuidKey);

        var json = RedisSerializer.json();
        t.setValueSerializer(json);
        t.setHashValueSerializer(json);

        t.afterPropertiesSet();
        return t;
    }
}
