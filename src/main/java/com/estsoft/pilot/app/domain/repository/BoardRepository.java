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

    @Query("SELECT COALESCE(MAX(b.thread), 0) + 1000 FROM BoardEntity b")
    Long findMaxBoardThread();

    @Query(nativeQuery = true, value = "SELECT COALESCE(MAX(b.thread), 0) FROM board b WHERE b.depth = 0 AND b.thread < ? ORDER BY b.thread DESC LIMIT 1")
    Long findByPrevThread(@Param("thread") Long thread);

    @Modifying
    @Query("UPDATE BoardEntity b SET b.thread = b.thread - 1 WHERE b.thread < :thread  AND b.thread > :prevThread")
    void updateBoardByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    void deleteById(@Param("id") Long id);

    @EntityGraph(value = "board-with-user", type = EntityGraphType.FETCH)
    @Override
    Page<BoardEntity> findAll(@Param("pageable") Pageable pageable);

    @EntityGraph(value = "board-with-user", type = EntityGraphType.FETCH)
    @Override
    Optional<BoardEntity> findById(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "SELECT b1.* " +
                    "FROM ( " +
                    "SELECT * " +
                    "FROM board  " +
                    "WHERE subject LIKE %?1% " +
                    "ORDER BY board_id desc " +
                    ") b1 " +
                    "JOIN board b2 " +
                    "ON (b1.board_id = b2.board_id) " +
                    "ORDER BY b1.thread desc, b1.depth desc "
            , countQuery = "SELECT COUNT(b.board_id) FROM board b WHERE b.subject LIKE %?1%"
    )
    Page<BoardEntity> findBySubject(@Param("subject") String subject, @Param("pageable") Pageable pageable);
}
