package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
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
@RequestMapping(value = "/api/v1/boards/{id}/comments")
public class CommentRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CommentService commentService;

    /**
     * 상세 게시글에 포함되어 있는 comment 목록을 보여준다.
     * @param id : board_id
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CommentDto>> reply(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.findByBoard(new BoardDto(id).toEntity()));
    }

    /**
     * 상세 게시글에 comment를 추가한다.
     * @param id : board_id
     * @param commentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> save(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
        commentService.saveAndUpdateComment(commentDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상세 게시글에 달린 comment에 댓글을 추가한다.
     * @param id : board_id
     * @param commentDto
     * @return
     * @throws BoardRestController.BoardNotFoundException
     */
    @PostMapping("/reply")
    public ResponseEntity<?> reply(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
        commentService.saveReplyByComment(id, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 코멘트 삭제(실제로는 삭제하지 않고 DELETE_YN 여부만 체크)
     * @param id
     * @return
     * @throws BoardNotFoundException
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws BoardNotFoundException {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
