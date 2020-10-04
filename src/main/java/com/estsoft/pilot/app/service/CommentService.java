package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.controller.BoardRestController;
import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
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
import java.util.Optional;

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
                .contents(commentEntity.getContents())
                .deleteYn(commentEntity.getDeleteYn())
                .createdDate(commentEntity.getCreatedDate())
                .boardEntity(commentEntity.getBoardEntity())
                .userEntity(commentEntity.getUserEntity())
                .modifiedDate(commentEntity.getModifiedDate())
                .build();
    }

    public CommentDto findOne(Long id) throws BoardNotFoundException {
        Optional<CommentEntity> commentEntityWrapper = commentRepository.findById(id);
        if (!commentEntityWrapper.isPresent()) {
            throw new BoardRestController.BoardNotFoundException();
        }
        return this.convertEntityToDto(commentEntityWrapper.get());
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
     * @param id : board_id
     * @param commentDto
     */
    public void saveReplyByComment(Long id, CommentDto commentDto) {
        Long thread = commentDto.getThread();
        Long prevThread = commentRepository.findByPrevCommentThread(thread, id);

        commentDto.setBoardEntity(new BoardDto(id).toEntity());
        commentRepository.updateCommentByThread(thread + 1, prevThread, commentDto.getBoardEntity());
        commentRepository.save(commentDto.toEntity()).getId();
    }

    /**
     * 코멘트 삭제여부 업데이트
     * @param id
     * @throws BoardNotFoundException
     */
    public void deleteById(Long id) throws BoardNotFoundException {
        CommentDto commentDto = this.findOne(id);
        commentDto.setDeleteYn("Y");

        commentRepository.save(commentDto.toEntity());
    }
}
