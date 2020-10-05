package com.estsoft.pilot;

import com.estsoft.pilot.app.domain.repository.CommentRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.dto.CommentDto;
import com.estsoft.pilot.app.service.CommentService;
import groovy.util.logging.Log;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;

/**
 * Create by madorik on 2020-10-02
 */
@SpringBootTest
@Log
@Commit
public class CommentRepositoryTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Test
    public void saveComment() {

        BoardDto boardDto = new BoardDto();
        boardDto.setId(2L);

        CommentDto commentDto = new CommentDto();
        commentDto.setUserName("TESTER");
        commentDto.setContents("contents");
        commentDto.setBoardEntity(boardDto.toEntity());

        commentRepository.save(commentDto.toEntity());

        Long[] arr = {302L, 301L, 296L};
        Arrays.stream(arr).forEach(num ->{
            /*IntStream.range(0, 10).forEach(i -> {
                CommentDto commentDto = new CommentDto();
                commentDto.setUserName("TESTER");
                commentDto.setContents("contents");
                commentDto.setBoardEntity(boardDto.toEntity());

                commentRepository.save(commentDto.toEntity());
            });*/
        });
    }

    @Test
    public void findByBoardId() {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(8L);

        Page<CommentDto> commentDtos = commentService.findByBoard(boardDto, 2);
        logger.info(commentDtos.toString());

    }
}
