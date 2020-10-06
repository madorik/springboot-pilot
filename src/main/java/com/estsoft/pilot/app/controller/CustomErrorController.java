package com.estsoft.pilot.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Create by madorik on 2020-09-25
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
            model.addAttribute("status", status.toString());
            model.addAttribute("message", httpStatus.getReasonPhrase());
            model.addAttribute("timestamp", new Date());
            model.addAttribute("home", "/boards");
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/404";
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/5xx";
            }
        }
        return "errors/denied";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
