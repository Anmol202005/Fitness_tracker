package org.fitness.fitness.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
 @Value("${REDIS_URL}")
 String redisUrl;
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        // Extract host and port from the Redis URL
        String[] redisUrlParts = redisUrl.replace("redis://", "").split(":");
        String host = redisUrlParts[0];
        int port = Integer.parseInt(redisUrlParts[1]);

        configuration.setHostName(host);
        configuration.setPort(port);

        // Optional: If you need to provide a password, set it here
        // configuration.setPassword(RedisPassword.of("your_password"));

        // Create and return a JedisConnectionFactory with the configuration
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> tokenTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use StringRedisSerializer for key and value
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}
