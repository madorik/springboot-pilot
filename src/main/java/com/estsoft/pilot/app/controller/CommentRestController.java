package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.dto.CommentDto;
import com.estsoft.pilot.app.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by madorik on 2020-10-01
 */
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/boards/{id}/comments")
public class CommentRestController {

    private final CommentService commentService;

    /**
     * 상세 게시글에 포함되어 있는 comment 목록을 보여준다.
     * @param id : board_id
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CommentDto>> reply(@PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        Page<CommentDto> commentDtoPages = commentService.findByBoard(new BoardDto(id), pageNum);
        return ResponseEntity.ok(commentDtoPages.getContent());
    }

    /**
     * 상세 게시글에 comment를 추가한다.
     * @param id : board_id
     * @param commentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> save(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
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
    public ResponseEntity<Void> reply(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
        commentService.saveReplyByComment(id, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 코멘트 수정
     * @param id : commentId
     * @return
     * @throws BoardNotFoundException
     */
    @PutMapping("/reply/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) throws BoardNotFoundException {
        commentService.saveComment(id, commentDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 코멘트 삭제(실제로는 삭제하지 않고 DELETE_YN 여부만 체크)
     * @param id : commentId
     * @return
     * @throws BoardNotFoundException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws BoardNotFoundException {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
