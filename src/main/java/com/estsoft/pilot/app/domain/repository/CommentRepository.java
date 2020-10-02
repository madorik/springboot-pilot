package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.boardEntity = ?1 ORDER BY c.thread DESC")
    List<CommentEntity> findByBoard(BoardEntity boardEntity);

    @Query(nativeQuery = true, value = "SELECT COALESCE(b.thread, 0) FROM comment b WHERE b.depth = 0 AND b.thread < ?1 AND b.board_id = ?2 LIMIT 1")
    Long findByPrevCommentThread(Long thread, Long boardId);

    @Query("UPDATE CommentEntity c SET c.thread = c.thread - 1 WHERE c.thread < :thread  AND c.thread > :prevThread")
    void updateCommentByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    @Query("SELECT COALESCE(MAX(c.thread), 0) + 1000 FROM CommentEntity c WHERE c.boardEntity.id = :boardId")
    Long findMaxCommentThreadByBoardId(Long boardId);
}
