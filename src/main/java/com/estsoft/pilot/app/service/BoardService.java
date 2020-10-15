package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.repository.BoardRepository;
import com.estsoft.pilot.app.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Create by madorik on 2020-09-20
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    private final CacheService cacheService;

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
     *
     * @param pageNum pageNum
     * @return Page<BoardDto>
     */
    @Transactional(readOnly = true)
    public Page<BoardDto> findAllBySubject(Integer pageNum, String subject) {
        String deleteYn = "N";
        Long offset = (pageNum - 1) * 20000L;
        Long totalCount = cacheService.getBoardTotalCount(subject, deleteYn);
        PageRequest of = PageRequest.of(pageNum - 1, PAGE_POST_COUNT);
        List<BoardEntity> boardEntityList = boardRepository.findBySubjectContainingAndDeleteYnIs(subject, deleteYn, offset);
        Page<BoardEntity> page = new PageImpl(boardEntityList, of, totalCount);

        return page.map(this::convertEntityToDto);
    }

    /**
     * 원글 thread 및 자식 답글의 삭제여부를 Y로 업데이트
     *
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
        cacheService.removeBoardTotalCount();
    }

    /**
     * 1. 답글을 등록하면 부모글 thread와 이전글 thread 사이의 thread를 -1씩 업데이트한다.
     * 2. 등록되는 댓글의 thread는 client에서 부모 thread의 -1되어 전송되었으므로 그대로 save한다.
     *
     * @param boardDto boardDto
     */
    public Long saveBoardReply(BoardDto boardDto) {
        Long thread = boardDto.getThread();
        Long prevThread = boardRepository.findByPrevThread(thread);
        boardRepository.updateBoardByThread(thread, prevThread);
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    /**
     * 1000단위의 max thread 값을 찾아 셋팅한뒤 저장
     *
     * @param boardDto boardDto
     */
    public Long saveAndUpdateBoard(BoardDto boardDto) {
        boardDto.setThread(boardRepository.findMaxBoardThread());
        Long id = boardRepository.save(boardDto.toEntity()).getId();
        cacheService.removeBoardTotalCount();
        return id;
    }

    /**
     * 제목, 내용 수정
     *
     * @param id       id
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
