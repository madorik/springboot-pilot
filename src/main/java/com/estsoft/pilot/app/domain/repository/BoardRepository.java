package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE BoardEntity b SET b.orderNo = b.orderNo + 1 WHERE b.pid = :pid AND b.orderNo > :orderNo")
    void updateBoardOrderNoByPid(@Param("pid") Long pid, @Param("orderNo") int orderNo);
}
