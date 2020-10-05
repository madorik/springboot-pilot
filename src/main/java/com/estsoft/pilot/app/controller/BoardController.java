package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
import com.estsoft.pilot.app.dto.BoardDto;
import com.estsoft.pilot.app.service.BoardService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Create by madorik on 2020-09-20
 */
@AllArgsConstructor
@RequestMapping("/boards")
@Controller
public class BoardController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BoardService boardService;

    @GetMapping
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "subject", defaultValue = "") String subject) {

        Page<BoardDto> boardList = boardService.findAllBySubject(pageNum, subject);
        model.addAttribute("boardList", boardList);
        return "board/board-list";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("requestFrom", "boardDto");
        return "board/board-save";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) throws BoardNotFoundException {
        BoardDto boardDto = boardService.findOne(id);
        model.addAttribute("boardDto", boardDto);
        return "board/board-view";
    }

    @GetMapping("/{id}/reply")
    public String reply(@PathVariable("id") Long id, Model model) throws BoardNotFoundException {
        BoardDto boardDto = boardService.findOne(id);
        model.addAttribute("boardDto", boardDto);
        return "board/board-reply";
    }
}
