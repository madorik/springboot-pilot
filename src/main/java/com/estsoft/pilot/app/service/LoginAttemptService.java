package com.estsoft.pilot.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Create by madorik on 2020-10-07
 */
@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class LoginAttemptService {

    private final CacheService cacheService;

    public void loginSucceeded(String remoteAddress) {
        cacheService.removeAttemptCount(remoteAddress);
    }

    public void loginFailed(String remoteAddress) {
        int count = 0;
        try {
            count = cacheService.getAttemptCount(remoteAddress);
        } catch (Exception e) {
            log.error("login attempt count exception : " + e);
        }
        count++;
        cacheService.putAttemptCount(remoteAddress, count);
    }

    public boolean isBlocked(String remoteAddress) {
        try {
            int maxAttemptCount = 5;
            return cacheService.getAttemptCount(remoteAddress) >= maxAttemptCount;
        } catch (Exception e) {
            log.error("login attempt count exception : " + e);
            return false;
        }
    }
}
