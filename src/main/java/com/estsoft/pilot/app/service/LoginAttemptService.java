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

    private final static Integer MAX_ATTEMPT_COUNT = 5;

    private final CacheService cacheService;

    public void loginSucceeded(String remoteAddress) {
        cacheService.removeAttemptCount(remoteAddress);
    }

    public void loginFailed(String remoteAddress) {
        int count = cacheService.getAttemptCount(remoteAddress) + 1;
        cacheService.putAttemptCount(remoteAddress, count);
    }

    public boolean isBlocked(String remoteAddress) {
        return cacheService.getAttemptCount(remoteAddress) >= MAX_ATTEMPT_COUNT;
    }
}
