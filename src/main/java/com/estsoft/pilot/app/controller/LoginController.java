package com.estsoft.pilot.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Create by madorik on 2020-09-20
 */
@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/")
    public String root(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            return "redirect:/boards";
        }
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal == null) {
            return "login";
        } else {
            return "redirect:/boards";
        }
    }

    @RequestMapping("/login/fail")
    public String loginError() {
        return "login";
    }
}
