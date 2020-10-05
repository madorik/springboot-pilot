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

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c JOIN FETCH c.boardEntity JOIN FETCH c.userEntity  WHERE c.boardEntity = ?1 ORDER BY c.thread DESC")
    List<CommentEntity> findByBoard(@Param("boardEntity") BoardEntity boardEntity);

    @Query(nativeQuery = true, value = "SELECT COALESCE(MAX(c.thread), 0) FROM comment c WHERE c.depth = 0 AND c.thread < ?1 AND c.board_id = ?2 ORDER BY c.thread DESC LIMIT 1")
    Long findByPrevCommentThread(@Param("thread") Long thread, @Param("boardId") Long boardId);

    @Modifying
    @Query("UPDATE CommentEntity c SET c.thread = c.thread - 1 WHERE c.thread < :thread  AND c.thread > :prevThread AND c.boardEntity = :boardEntity")
    void updateCommentByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread, @Param("boardEntity") BoardEntity boardEntity);

    @Query("SELECT COALESCE(MAX(c.thread), 0) + 1000 FROM CommentEntity c WHERE c.boardEntity.id = :boardId")
    Long findMaxCommentThreadByBoardId(@Param("boardId") Long boardId);

    @EntityGraph(value = "comment-with-all", type = EntityGraph.EntityGraphType.FETCH)
    Page<CommentEntity> findAllByBoardEntity(BoardEntity boardEntity, Pageable pageable);
}
