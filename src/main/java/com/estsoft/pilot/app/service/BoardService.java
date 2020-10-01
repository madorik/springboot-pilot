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

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Create by madorik on 2020-09-20
 */
@AllArgsConstructor
@Service
@Transactional
public class BoardService {
    private BoardRepository boardRepository;
    private static final int PAGE_POST_COUNT = 20;

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .thread(boardEntity.getThread())
                .depth(boardEntity.getDepth())
                .userId(boardEntity.getUserId())
                .userName(boardEntity.getUserName())
                .subject(boardEntity.getSubject())
                .contents(boardEntity.getContents())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();
    }

    public BoardDto findOne(Long id) throws BoardNotFoundException {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        if (!boardEntityWrapper.isPresent()) {
            throw new BoardNotFoundException();
        }
        return this.convertEntityToDto(boardEntityWrapper.get());
    }

    /**
     * page 단위로 게시글 가져오기
     * @param pageNum
     * @return Page<BoardDto>
     */
    public Page<BoardDto> findAllByOrderByIdDesc(Integer pageNum) {
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT,
                Sort.by("thread").descending().and(Sort.by("depth").descending())));
        return page.map(this::convertEntityToDto);
    }

    public Long save(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 1. 답글을 등록하면 부모글 thread와 이전글 thread 사이의 thread를 -1씩 업데이트한다.
     * 2. 등록되는 댓글의 thrad는 client에서 부모 threa의 -1되어 전송되었으므로 그대로 save한다.
     * @param boardDto
     * @throws BoardNotFoundException
     */
    public void saveBoardReply(BoardDto boardDto) throws BoardNotFoundException {
        Long thread = boardDto.getThread();
        Long prevThread = boardRepository.findByPrevThread(thread);
        boardRepository.updateBoardByThread(thread + 1, prevThread);
        boardRepository.save(boardDto.toEntity()).getId();
    }

    /**
     * 1000단위의 max thread 값을 찾아 셋팅한뒤 저장
     * @param boardDto
     * @return
     * @throws BoardNotFoundException
     */
    public BoardEntity saveAndUpdateBoard(BoardDto boardDto) throws BoardNotFoundException {
        boardDto.setThread(boardRepository.findMaxBoardThread());
        boardRepository.save(boardDto.toEntity());
        return boardDto.toEntity();
    }

    /**
     * 제목, 내용 수정
     * @param id
     * @param boardDto
     * @throws BoardNotFoundException
     */
    public void edit(Long id, BoardDto boardDto) throws BoardNotFoundException {
        BoardDto copyBoardDto = this.findOne(id);
        BoardEntity boardEntity = copyBoardDto.toEntity();
        boardEntity.update(boardDto.getSubject(), boardDto.getContents());
        boardRepository.save(boardEntity);
    }
}
