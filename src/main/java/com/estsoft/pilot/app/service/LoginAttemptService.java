package com.estsoft.pilot.app.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Create by madorik on 2020-10-07
 */
@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;

    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String remoteAddress) {
        attemptsCache.invalidate(remoteAddress);
    }

    public void loginFailed(String remoteAddress) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(remoteAddress);
        } catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(remoteAddress, attempts);
    }

    public boolean isBlocked(String remoteAddress) {
        try {
            return attemptsCache.get(remoteAddress) >= MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
    }
}
