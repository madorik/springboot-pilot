package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import com.estsoft.pilot.app.domain.repository.CommentRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.dto.CommentDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by madorik on 2020-10-01
 */
@AllArgsConstructor
@Service
@Transactional
public class CommentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CommentRepository commentRepository;
    private static final int PAGE_POST_COUNT = 20;

    private CommentDto convertEntityToDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .thread(commentEntity.getThread())
                .depth(commentEntity.getDepth())
                .userName(commentEntity.getUserName())
                .contents(commentEntity.getContents())
                .deleteYn(commentEntity.getDeleteYn())
                .createdDate(commentEntity.getCreatedDate())
                //.boardEntity(commentEntity.getBoardEntity())
                .modifiedDate(commentEntity.getModifiedDate())
                .build();
    }

    public List<CommentDto> findByBoard(BoardEntity boardEntity) {
        List<CommentEntity> commentEntities = commentRepository.findByBoard(boardEntity);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            commentDtoList.add(this.convertEntityToDto(commentEntity));
        }
        return commentDtoList;
    }

    /**
     * 상세 게시글에 코멘트 추가.
     * @param commentDto
     * @param boardId
     * @return
     */
    public CommentEntity saveAndUpdateComment(CommentDto commentDto, Long boardId) {
        commentDto.setBoardEntity(new BoardDto(boardId).toEntity());
        commentDto.setThread(commentRepository.findMaxCommentThreadByBoardId(boardId));
        commentRepository.save(commentDto.toEntity());
        return commentDto.toEntity();
    }

    /**
     * 상세 게시글 코멘트에 댓글을 추가한다. (대댓글)
     * @param boardId
     * @param commentDto
     */
    public void saveReplyByComment(Long boardId, CommentDto commentDto) {
        Long thread = commentDto.getThread();
        Long prevThread = commentRepository.findByPrevCommentThread(thread, boardId);
        commentDto.setBoardEntity(new BoardDto(boardId).toEntity());
        commentRepository.updateCommentByThread(thread + 1, prevThread, commentDto.getBoardEntity());
        commentRepository.save(commentDto.toEntity()).getId();
    }
}
