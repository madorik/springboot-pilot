package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 게시글 추가시에 설정할 thread 조회
     *
     * @return 마지막 thread + 1000
     */
    @Query("SELECT COALESCE(MAX(b.thread), 0) + 1000 FROM BoardEntity b")
    Long findMaxBoardThread();

    /**
     * 이전 게시글의 thread number 조회
     *
     * @param thread thread
     * @return id
     */
    @Query(nativeQuery = true,
            value = "SELECT COALESCE(MAX(b.thread), 0) "
                    + "FROM board AS b "
                    + "JOIN ("
                    + "       SELECT board_id "
                    + "       FROM board "
                    + "       WHERE depth = 0 "
                    + "       AND thread < :thread "
                    + "       ORDER BY thread DESC "
                    + "       LIMIT 1 "
                    + ") AS t "
                    + "ON t.board_id = b.board_id")
    Long findByPrevThread(@Param("thread") Long thread);

    /**
     * 게시글에 달린 답변 thread number 업데이트
     *
     * @param thread     thread
     * @param prevThread prevThread
     */
    @Modifying
    @Query("UPDATE BoardEntity b SET b.thread = b.thread - 1 WHERE b.thread <= :thread  AND b.thread > :prevThread")
    void updateBoardByThread(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    /**
     * 게시글 상세조회
     *
     * @param id id
     * @return Optional<BoardEntity>
     */
    @EntityGraph(value = "board-with-user", type = EntityGraphType.FETCH)
    @Override
    Optional<BoardEntity> findById(@Param("id") Long id);

    /**
     * 원글 삭제여부 업데이트 & total count 초기화
     *
     * @param thread     thread
     * @param prevThread prevThread
     */
    @Modifying
    @Query("UPDATE BoardEntity b SET b.deleteYn = 'Y' WHERE b.thread <= :thread  AND b.thread > :prevThread")
    void deleteByThreadRange(@Param("thread") Long thread, @Param("prevThread") Long prevThread);

    /**
     * 전체 개수 조회
     *
     * @param subject subject
     * @return total count
     */
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM board b WHERE subject LIKE :subject% ")
    Long findBySubjectTotalCount(@Param("subject") String subject);

    /**
     * 게시글 페이징 처리
     *
     * @param subject  subject
     * @param offset   offset
     * @return List<BoardEntity>
     */
    @Query(nativeQuery = true,
            value = "SELECT b.board_id, b.user_id, b.subject, b.contents, b.delete_yn, b.depth, b.thread, b.created_date, b.modified_date "
                    + "FROM board AS b "
                    + "JOIN ("
                    + "       SELECT board_id "
                    + "       FROM board "
                    + "       WHERE subject LIKE :subject% "
                    + "       AND thread <= (SELECT max(thread) - :offset FROM board) "
                    + "       ORDER BY thread DESC "
                    + "       LIMIT 20 "
                    + ") AS t "
                    + "ON t.board_id = b.board_id")
    List<BoardEntity> findBySubject(@Param("subject") String subject, @Param("offset") Long offset);

}
