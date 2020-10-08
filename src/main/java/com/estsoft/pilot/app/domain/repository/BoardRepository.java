package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 게시글 추가시에 설정할 thread 조회
     * @return 마지막 thread + 1000
     */
    @Query("SELECT COALESCE(MAX(b.thread), 0) + 1000 FROM BoardEntity b")
    Long findMaxBoardThread();

    /**
     * 이전 게시글의 thread number 조회
     * @param thread
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT COALESCE(MAX(b.thread), 0) FROM board b WHERE b.depth = 0 AND b.thread < ? ORDER BY b.thread DESC LIMIT 1")
    Long findByPrevThread(@Param("thread") Long thread);

    /**
     * 게시글에 달린 답변 thread number 업데이트
     * @param thread
     * @param prevThread
     */
    @Modifying
    @Query("UPDATE BoardEntity b SET b.thread = b.thread - 1 WHERE b.thread <= :thread  AND b.thread > :prevThread")
    void updateBoardByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    /**
     * 게시글 상세조회
     * @param id
     * @return
     */
    @EntityGraph(value = "board-with-user", type = EntityGraphType.FETCH)
    @Override
    Optional<BoardEntity> findById(@Param("id") Long id);

    /**
     * 게시글 페이징 조회
     * @param subject
     * @param pageable
     * @return
     */
    @Query(value = "SELECT b FROM BoardEntity b JOIN FETCH b.userEntity WHERE b.subject LIKE %:subject% AND b.deleteYn = 'N'"
        , countQuery = "SELECT COUNT(b.id) FROM BoardEntity b WHERE b.subject LIKE %:subject% AND b.deleteYn = 'N'")
    Page<BoardEntity> findBySubject(@Param("subject") String subject, @Param("pageable") Pageable pageable);

    /**
     * 원글 삭제여부 업데이트
     * @param thread
     * @param prevThread
     */
    @Modifying
    @Query("UPDATE BoardEntity b SET b.deleteYn = 'Y' WHERE b.thread <= :thread  AND b.thread > :prevThread")
    void deleteByThreadRange(Long thread, Long prevThread);
}
