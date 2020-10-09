package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /**
     * 새 코멘트 추가시에 설정할 thread 조회
     * @param boardId boardId
     * @return 마지막 thread + 1000
     */
    @Query("SELECT COALESCE(MAX(c.thread), 0) + 1000 FROM CommentEntity c WHERE c.boardEntity.id = :boardId")
    Long findMaxCommentThreadByBoardId(@Param("boardId") Long boardId);

    /**
     * 현재 게시글의 이전 코멘트 thread number 조회
     * @param thread thread
     * @param boardId boardId
     * @return prevThread
     */
    @Query(nativeQuery = true, value = "SELECT COALESCE(MAX(c.thread), 0) FROM comment c WHERE c.depth = 0 AND c.thread < ?1 AND c.board_id = ?2 ORDER BY c.thread DESC LIMIT 1")
    Long findByPrevCommentThread(@Param("thread") Long thread, @Param("boardId") Long boardId);

    /**
     * 코멘트에 달린 thread number 업데이트
     * @param thread thread
     * @param prevThread prevThread
     * @param boardEntity boardEntity
     */
    @Modifying
    @Query("UPDATE CommentEntity c SET c.thread = c.thread - 1 WHERE c.thread < :thread  AND c.thread > :prevThread AND c.boardEntity = :boardEntity")
    void updateCommentByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread, @Param("boardEntity") BoardEntity boardEntity);

    /**
     * 상세 게시글에 추가된 코멘트 조회
     * @param boardEntity boardEntity
     * @param pageable pageable pageable
     * @return Page<CommentEntity>
     */
    @EntityGraph(value = "comment-with-all", type = EntityGraph.EntityGraphType.FETCH)
    Page<CommentEntity> findAllByBoardEntity(BoardEntity boardEntity, Pageable pageable);
}
