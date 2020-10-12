package com.estsoft.pilot;

import com.estsoft.pilot.app.domain.repository.FileRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.service.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by madorik on 2020-10-12
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private BoardService boardService;


    @Test
    @Transactional
    @Description("이미지 소스에서 게시글에 등록된 이미지 id 추출")
    public void getImageIdFromSrc() throws Exception {
        Long boardId = 2720002L;
        BoardDto boardDto = boardService.findOne(boardId);


        Pattern nonValidPattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        List<Long> result = new ArrayList<>();
        Matcher matcher = nonValidPattern.matcher(boardDto.getContents());
        while (matcher.find()) {
            result.add(Long.valueOf(matcher.group(1).split("/")[4]));
        }
        fileRepository.updateBoardIdByIdIn(boardId, result);

       // List<FileEntity> fileDto = fileRepository.findByIdIn(result);

        logger.info("result >> " + result);
    }
}
