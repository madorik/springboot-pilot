package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.boardId = :boardId WHERE f.id IN :fileIdList")
    void updateBoardIdByIdIn(@Param("boardId") Long boardId, @Param("fileIdList") List<Long> fileIdList);

    void deleteByBoardId(@Param("boardId") Long boardId);

    List<FileEntity> findByCommentId(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.commentId = :commentId WHERE f.id IN :fileIdList")
    void updateCommentIdByIdIn(@Param("commentId") Long commentId, @Param("fileIdList") List<Long> fileIdList);

    void deleteByCommentId(@Param("commentId") Long commentId);


}
