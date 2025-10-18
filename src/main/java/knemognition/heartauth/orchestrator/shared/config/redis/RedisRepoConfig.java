package knemognition.heartauth.orchestrator.shared.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories()
public class RedisRepoConfig {
    @Configuration
    public class RedisBinaryConfig {

        @Bean
        public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory cf) {
            RedisTemplate<String, byte[]> template = new RedisTemplate<>();
            template.setConnectionFactory(cf);
            // Binary-safe serialization
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer((RedisSerializer<?>) new ByteArrayRedisSerializer());
            template.afterPropertiesSet();
            return template;
        }

        public static class ByteArrayRedisSerializer implements RedisSerializer<byte[]> {
            @Override
            public byte[] serialize(byte[] bytes) {
                return bytes;
            }

            @Override
            public byte[] deserialize(byte[] bytes) {
                return bytes;
            }
        }
    }

}
