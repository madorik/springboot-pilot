package com.estsoft.pilot;

import com.estsoft.pilot.app.controller.BoardRestController;
import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.UserEntity;
import com.estsoft.pilot.app.domain.repository.BoardRepository;
import com.estsoft.pilot.app.domain.repository.UserRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.dto.UserDto;
import com.estsoft.pilot.app.service.BoardService;
import com.estsoft.pilot.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Create by madorik on 2020-09-28
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardControllerTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager entityManager;

    UserRepository userRepository;


    @Test
    @Description("게시글 단일 조회")
    public void findBoard() {
        Optional<BoardEntity> boardEntity = boardRepository.findById(1L);
        logger.info(boardEntity.get().toString());
    }

    @Test
    @Description("게시글 생성")
    public void savePost() {
        BoardDto boardDto = new BoardDto();
        boardDto.setSubject("SUBJECT");
        boardDto.setContents("CONTENTS");
        //boardDto.setUserId("tester@gmail.com");

        boardService.saveAndUpdateBoard(boardDto);
    }

    @Test
    @Description("게시글의 댓글 생성")
    public void saveReply() {
        BoardDto boardDto = new BoardDto();
        boardDto.setSubject("SUBJECT");
        boardDto.setContents("CONTENTS");
       // boardDto.setUserId("tester@gmail.com");
        boardDto.setThread(1000L);
        boardDto.setDepth(1);

        boardService.saveAndUpdateBoard(boardDto);
    }

    @Test
    @Description("게시글 더미 데이터 생성")
    public void loopSave() {
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                  //  .userEntity("TESTER")
                    .thread(i + 1000L)
                    .depth(0)
                    .subject("TEST SUBJECT - " + i)
                    .contents("TEST CONTENTS - " + i)
                    .build());
            boardRepository.save(boardEntity);
        });
    }

    @Test
    @Description("prev thread check")
    public void findByPrevThread() {
        logger.info("[[[[[[[[[[ " + boardRepository.findByPrevThread(3000L) + " ]]]]]]]]]]]]]");
    }

    @Test
    @Description("게시판 max id")
    public void getMaxId() {
        Long result = boardRepository.findMaxBoardThread();
        logger.info("[[[[[[[[[[ " + result + " ]]]]]]]]]]]]]");
    }

    @Test
    @Description("게시판 검색")
    public void search() {
        Page<BoardDto> boardDtoPage = boardService.findAllBySubject(1, "123");
        logger.info("boardDtoPage >>> " + boardDtoPage.getContent().toString());
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        Optional<UserEntity> userEntity = userRepository.findById(1L);

        return BoardDto.builder()
                .id(boardEntity.getId())
                .thread(boardEntity.getThread())
                .depth(boardEntity.getDepth())
                .userEntity(userEntity.get())
                .subject(boardEntity.getSubject())
                .contents(boardEntity.getContents())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();
    }
}
