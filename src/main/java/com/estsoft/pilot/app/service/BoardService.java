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
                .pid(boardEntity.getPid())
                .orderNo(boardEntity.getOrderNo())
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
                Sort.by("pid").descending().and(Sort.by("orderNo").ascending())));
        return page.map(this::convertEntityToDto);
    }

    public Long getBoardCount() {
        return boardRepository.count();
    }

    public Long save(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 답글을 등록하면 원글에 등록되어 있는 답글의 orderNo를 +1 해준다.
     * @param boardDto
     * @throws BoardNotFoundException
     */
    public void saveBoardReply(BoardDto boardDto) throws BoardNotFoundException {
        BoardDto originBoard = this.findOne(boardDto.getPid());
        boardRepository.updateBoardOrderNoByPid(originBoard.getPid(), originBoard.getOrderNo());
        boardRepository.save(boardDto.toEntity()).getId();
    }

    /**
     * 원글의 entity id와 pid를 동일하게 맞춰준다.
     * @param boardDto
     * @return
     * @throws BoardNotFoundException
     */
    public BoardEntity saveAndUpdateBoard(BoardDto boardDto) throws BoardNotFoundException {
        Long id = boardRepository.save(boardDto.toEntity()).getId();
        BoardDto updateBoardDto = this.findOne(id);
        updateBoardDto.setPid(id);
        boardRepository.save(updateBoardDto.toEntity());
        return updateBoardDto.toEntity();
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
