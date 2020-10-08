package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.controller.BoardRestController.BoardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Create by madorik on 2020-10-05
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<Void> handleBoardNotFoundException(BoardNotFoundException e) {
        log.error("handleBoardNotFoundException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> handleException(Exception e) {
        log.error("handleException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
