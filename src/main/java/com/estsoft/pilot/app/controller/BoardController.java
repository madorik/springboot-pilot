package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by madorik on 2020-09-20
 */
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.findAllByOrderByIdDesc(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);
        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/board-list";
    }

    @GetMapping("/board/save")
    public String save(Model model) {
        model.addAttribute("requestFrom", "boardDto");
        return "board/board-save";
    }

    @ResponseBody
    @PostMapping("/board/save")
    public Long save(@RequestBody BoardDto boardDto) {
        return boardService.save(boardDto);
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.findOne(id);
        model.addAttribute("boardDto", boardDto);
        return "board/board-view";
    }

    @ResponseBody
    @PutMapping("/board/edit/{id}")
    public Long edit(@RequestBody BoardDto boardDto) {
        return boardService.save(boardDto);
    }

    @DeleteMapping("/board/{id}")
    public void delete(@PathVariable("id") Long id) {
        boardService.delete(id);
    }
}
