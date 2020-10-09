package com.estsoft.pilot.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create by madorik on 2020-09-20
 */
@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login/fail")
    public String loginError() {
        return "login";
    }
}
