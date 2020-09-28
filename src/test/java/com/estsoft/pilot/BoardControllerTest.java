package com.estsoft.pilot;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

/**
 * Create by madorik on 2020-09-28
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardControllerTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    @Description("게시글 더미 데이터 생성")
    public void save() {
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                    .userId("TESTER")
                    .userName("TESTER")
                    .subject("TEST SUBJECT - " + i)
                    .contents("TEST CONTENTS - " + i)
                    .build());
            boardRepository.save(boardEntity);
        });
    }

    @Test
    @Description("게시글 조회")
    public void get() {

    }
}
