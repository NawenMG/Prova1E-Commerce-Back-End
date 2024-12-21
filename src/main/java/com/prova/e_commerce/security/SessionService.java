package com.prova.e_commerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionService {

    @Autowired
    private RedisTemplate<String, SessionData> redisTemplate;

    private static final long SESSION_TIMEOUT = 1; // Timeout della sessione in ore

    public void saveSession(String sessionId, SessionData data) {
        redisTemplate.opsForValue().set(sessionId, data, SESSION_TIMEOUT, TimeUnit.HOURS);
    }

    public SessionData getSession(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }

    public void invalidateSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
