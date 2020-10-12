package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.service.BoardService;
import com.estsoft.pilot.app.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Create by madorik on 2020-09-24
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/boards")
@Slf4j
public class BoardRestController {

    private final BoardService boardService;

    private final FileService fileService;

    /**
     * 게시글 상세조회
     *
     * @param id id
     * @return ResponseEntity<BoardDto>
     * @throws BoardNotFoundException BoardNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> detail(@PathVariable("id") Long id) throws BoardNotFoundException {
        BoardDto boardDto = boardService.findOne(id);
        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    /**
     * 원글 등록
     *
     * @param boardDto boardDto
     * @return ResponseEntity<Void>
     */
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody BoardDto boardDto) {
        Long boardId = boardService.saveAndUpdateBoard(boardDto);
        fileService.updateBoardIdByIdIn(boardId, boardDto.getContents());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 원글 수정
     *
     * @param boardDto boardDto
     * @return ResponseEntity<Void>
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable("id") Long boardId, @RequestBody BoardDto boardDto) throws BoardNotFoundException {
        boardService.edit(boardId, boardDto);
        fileService.updateBoardIdByIdIn(boardId, boardDto.getContents());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 게시글 삭제
     *
     * @param id id
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws BoardNotFoundException {
        boardService.deleteById(id);
        fileService.deleteByBoardId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 답글 등록
     *
     * @param boardDto boardDto
     * @return ResponseEntity<Void>
     */
    @PostMapping("/{id}/reply")
    public ResponseEntity<Void> reply(@RequestBody BoardDto boardDto) {
        Long boardId = boardService.saveBoardReply(boardDto);
        fileService.updateBoardIdByIdIn(boardId, boardDto.getContents());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    public static class BoardNotFoundException extends Exception {
        public BoardNotFoundException() {
            super();
        }
    }
}
