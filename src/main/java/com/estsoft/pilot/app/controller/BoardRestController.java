package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.service.BoardService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Create by madorik on 2020-09-24
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/boards")
public class BoardRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BoardService boardService;

    /**
     * 게시글 상세조회
     * @param id
     * @param model
     * @return
     * @throws BoardRestController.BoardNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id, Model model) throws BoardNotFoundException {
        logger.info("boards detail");
        BoardDto boardDto = boardService.findOne(id);
        return new ResponseEntity<BoardDto>(boardDto, HttpStatus.OK);
    }

    /**
     * 원글 등록
     * @param boardDto
     * @return
     * @throws BoardNotFoundException
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody BoardDto boardDto) throws BoardNotFoundException {
        boardService.saveAndUpdateBoard(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 원글 수정
     * @param boardDto
     * @return
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") Long id, @RequestBody BoardDto boardDto) throws BoardNotFoundException {
        boardService.edit(id, boardDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 게시글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 답글 등록
     * @param id
     * @param boardDto
     * @return
     * @throws BoardNotFoundException
     */
    @PostMapping("/{id}/reply")
    public ResponseEntity<?> reply(@PathVariable("id") Long id, @RequestBody BoardDto boardDto) throws BoardNotFoundException {
        boardService.saveBoardReply(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public static class BoardNotFoundException extends Exception {
        public BoardNotFoundException() {
            super();
        }

        public BoardNotFoundException(String message) {
            super(message);
        }

        public BoardNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public BoardNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
