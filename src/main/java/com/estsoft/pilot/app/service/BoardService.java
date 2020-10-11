package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.repository.BoardRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Create by madorik on 2020-09-20
 */
@AllArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    private static final int PAGE_POST_COUNT = 20;

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .thread(boardEntity.getThread())
                .depth(boardEntity.getDepth())
                .userEntity(boardEntity.getUserEntity())
                .subject(boardEntity.getSubject())
                .contents(boardEntity.getContents())
                .deleteYn(boardEntity.getDeleteYn())
                .commentEntities(boardEntity.getCommentEntities())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public BoardDto findOne(Long id) throws BoardNotFoundException {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        if (boardEntityWrapper.isEmpty()) {
            throw new BoardNotFoundException();
        }
        return this.convertEntityToDto(boardEntityWrapper.get());
    }

    /**
     * page 단위로 게시글 가져오기
     * @param pageNum pageNum
     * @return Page<BoardDto>
     */
    @Transactional(readOnly = true)
    public Page<BoardDto> findAllBySubject(Integer pageNum, String subject) {
        Page<BoardEntity> page = boardRepository.findBySubjectIgnoreCaseContainingAndDeleteYnIs(subject, "N", PageRequest.of(pageNum - 1, PAGE_POST_COUNT,
                Sort.by("thread").descending()));
        return page.map(this::convertEntityToDto);
    }

    public Long save(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    /**
     * 원글 thread 및 자식 답글의 삭제여부를 Y로 업데이트
     * @param id id
     * @throws BoardNotFoundException BoardNotFoundException
     */
    public void deleteById(Long id) throws BoardNotFoundException {
        BoardDto boardDto = this.findOne(id);
        int depth = boardDto.getDepth();
        if (depth == 0) {
            // 원글일 경우 자식 답글까지 업데이트
            Long thread = boardDto.getThread();
            Long prevThread = boardRepository.findByPrevThread(thread);
            boardRepository.deleteByThreadRange(thread, prevThread);
        } else {
            // 답글일 경우 자신만 업데이트
            boardDto.setDeleteYn("Y");
            boardRepository.save(boardDto.toEntity());
        }
    }

    /**
     * 1. 답글을 등록하면 부모글 thread와 이전글 thread 사이의 thread를 -1씩 업데이트한다.
     * 2. 등록되는 댓글의 thread는 client에서 부모 thread의 -1되어 전송되었으므로 그대로 save한다.
     * @param boardDto boardDto
     */
    public void saveBoardReply(BoardDto boardDto) {
        Long thread = boardDto.getThread();
        Long prevThread = boardRepository.findByPrevThread(thread);
        boardRepository.updateBoardByThread(thread, prevThread);
        boardRepository.save(boardDto.toEntity());
    }

    /**
     * 1000단위의 max thread 값을 찾아 셋팅한뒤 저장
     * @param boardDto boardDto
     */
    public void saveAndUpdateBoard(BoardDto boardDto) {
        boardDto.setThread(boardRepository.findMaxBoardThread());
        boardRepository.save(boardDto.toEntity());
    }

    /**
     * 제목, 내용 수정
     * @param id id
     * @param boardDto boardDto
     * @throws BoardNotFoundException BoardNotFoundException
     */
    public void edit(Long id, BoardDto boardDto) throws BoardNotFoundException {
        BoardDto copyBoardDto = this.findOne(id);
        BoardEntity boardEntity = copyBoardDto.toEntity();
        boardEntity.update(boardDto.getSubject(), boardDto.getContents());
        boardRepository.save(boardEntity);
    }
}
