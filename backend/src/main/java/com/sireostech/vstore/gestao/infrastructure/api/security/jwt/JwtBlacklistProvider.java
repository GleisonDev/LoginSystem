package com.sireostech.vstore.gestao.infrastructure.api.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class JwtBlacklistProvider {

    private static final Duration DEFAULT_EXPIRATION = Duration.ofHours(2);

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String token, Duration durationTTL) {

        redisTemplate.opsForValue().set(token, "invalidated", durationTTL);

    }

    public boolean isTokenBlacklisted(String token) {

        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}