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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Create by madorik on 2020-09-20
 */
@AllArgsConstructor
@Service
@Transactional
public class BoardService {
    private BoardRepository boardRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 5;  // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 10;       // 한 페이지에 존재하는 게시글 수

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
        BoardEntity boardEntity = boardEntityWrapper.get();
        return this.convertEntityToDto(boardEntity);
    }

    public List<BoardDto> findAllByOrderByIdDesc(Integer pageNum) {
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT,
                Sort.by("pid").descending().and(Sort.by("orderNo").ascending())));
        List<BoardEntity> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    public Integer[] getPageList(Integer pageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        // 총 게시글 갯수
        Double totalCnt = Double.valueOf(this.getBoardCount());

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int) (Math.ceil((totalCnt / PAGE_POST_COUNT)));

        Integer blockLastPageNum = (totalLastPageNum > pageNum + BLOCK_PAGE_NUM_COUNT)
                ? pageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        pageNum = (pageNum <= 9) ? 1 : pageNum - 2;

        // 페이지 번호 할당
        for (int val = pageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
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
