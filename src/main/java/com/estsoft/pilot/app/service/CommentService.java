package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import com.estsoft.pilot.app.domain.repository.CommentRepository;
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

    public void saveBoardComment(Long boardId, CommentDto commentDto) {
        Long thread = commentDto.getThread();
        Long prevThread = commentRepository.findByPrevCommentThread(thread, boardId);
        commentRepository.updateCommentByThread(thread + 1, prevThread);
        commentRepository.save(commentDto.toEntity()).getId();
    }

    public CommentEntity saveAndUpdateComment(CommentDto commentDto, Long boardId) {
        commentDto.setThread(commentRepository.findMaxCommentThreadByBoardId(boardId));
        commentRepository.save(commentDto.toEntity());
        return commentDto.toEntity();
    }
}
