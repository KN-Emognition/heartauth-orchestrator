package knemognition.heartauth.orchestrator.shared.config.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@Configuration
@EnableRedisRepositories(basePackages = {
        "knemognition.heartauth.orchestrator.challenges.infrastructure.persistence",
        "knemognition.heartauth.orchestrator.pairings.infrastructure.persistence"
})
public class RedisConfig {
}