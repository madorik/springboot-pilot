package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT COALESCE(MAX(b.thread), 0) + 1000 FROM BoardEntity b")
    Long findMaxBoardThread();

    @Query(nativeQuery = true, value = "SELECT COALESCE(b.thread, 0) FROM board b WHERE b.depth = 0 AND b.thread < ? ORDER BY b.thread DESC LIMIT 1")
    Long findByPrevThread(@Param("thread") Long thread);

    @Modifying
    @Query("UPDATE BoardEntity b SET b.thread = b.thread - 1 WHERE b.thread < :thread  AND b.thread > :prevThread")
    void updateBoardByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    void deleteById(Long id);
}
