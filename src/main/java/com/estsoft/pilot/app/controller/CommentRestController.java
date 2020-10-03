package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.dto.CommentDto;
import com.estsoft.pilot.app.service.CommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by madorik on 2020-10-01
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CommentService commentService;

    /**
     * 상세 게시글에 포함되어 있는 comment 목록을 보여준다.
     * @param boardId
     * @return
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentDto>> reply(@PathVariable("boardId") Long boardId) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(boardId);
        return ResponseEntity.ok(commentService.findByBoard(boardDto.toEntity()));
    }

    /**
     * 상세 게시글에서 comment를 추가한다.
     * @param boardId
     * @param commentDto
     * @return
     */
    @PostMapping("/{boardId}")
    public ResponseEntity<?> save(@PathVariable("boardId") Long boardId, @RequestBody CommentDto commentDto) {
        commentService.saveAndUpdateComment(commentDto, boardId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상세 게시글에 달린 comment에 댓글을 추가한다.
     * @param boardId
     * @param commentDto
     * @return
     * @throws BoardRestController.BoardNotFoundException
     */
    @PostMapping("/{boardId}/reply")
    public ResponseEntity<?> reply(@PathVariable("boardId") Long boardId, @RequestBody CommentDto commentDto) throws BoardRestController.BoardNotFoundException {
        commentService.saveReplyByComment(boardId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
