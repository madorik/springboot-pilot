package com.estsoft.pilot.app.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Create by madorik on 2020-10-07
 */
@Slf4j
@Service
public class LoginAttemptService {

    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String remoteAddress) {
        attemptsCache.invalidate(remoteAddress);
    }

    public void loginFailed(String remoteAddress) {
        int attemptCount = 0;
        try {
            attemptCount = attemptsCache.get(remoteAddress);
        } catch (Exception e) {
            log.error("loginFailed {}", e);
        }
        attemptCount++;
        attemptsCache.put(remoteAddress, attemptCount);
    }

    public boolean isBlocked(String remoteAddress) {
        try {
            int maxAttemptCount = 5;
            return attemptsCache.get(remoteAddress) >= maxAttemptCount;
        } catch (Exception e) {
            return false;
        }
    }
}
