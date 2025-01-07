package org.fitness.fitness.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
private RedisTemplate<String, String> tokenTemplate;

public void storeToken(String email, String token, long ttlInSeconds) {
    // Store token with expiration time
    tokenTemplate.opsForValue().set("TOKEN_" + email, token, ttlInSeconds, TimeUnit.SECONDS);
}

public boolean isTokenValid(String email, String token) {
    String storedToken = (String) tokenTemplate.opsForValue().get("TOKEN_" + email);
    if (storedToken == null) {
        return false; // Token doesn't exist, so it's invalid
    }
    return token.equals(storedToken); // Return true if the token matches
}


public void invalidateToken(String email) {
    tokenTemplate.delete("TOKEN_" + email); // Remove the token from Redis
}

 public void invalidateAllTokens(String email) {
        // Find all keys matching the pattern
        String pattern = "TOKEN_" + email + "*";
        Set<String> keys = tokenTemplate.keys(pattern);

        // Delete all matching keys
        if (keys != null && !keys.isEmpty()) {
            tokenTemplate.delete(keys);
        }
    }

}
