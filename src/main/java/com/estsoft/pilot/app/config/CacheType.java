package com.estsoft.pilot.app.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Create by madorik on 2020-10-17
 */
@Getter
@AllArgsConstructor
public enum CacheType {

    TOTAL_COUNT("totalCountCache", 60 * 60, 100),
    ATTEMPT_COUNT("attemptCountCache", 60 * 60, 100);

    private final String cacheName;

    private final int expiredAfterWrite;

    private final int maximumSize;
}