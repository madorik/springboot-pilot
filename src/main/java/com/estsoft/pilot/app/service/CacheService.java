package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.domain.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create by madorik on 2020-09-20
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class CacheService {

    private final BoardRepository boardRepository;

    @Cacheable(value = "totalCountCache", key = "#subject")
    public Long getBoardTotalCount(String subject) {
        return boardRepository.findBySubjectTotalCount(subject);
    }

    @CacheEvict(value = "totalCountCache", allEntries = true)
    public void removeBoardTotalCount() { }
}
