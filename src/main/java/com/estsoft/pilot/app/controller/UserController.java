package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.UserDto;
import com.estsoft.pilot.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create by madorik on 2020-09-21
 */
@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/user/signup")
    public String signup() {
        return "/user/signup";
    }

    @PostMapping("/user/signup")
    public String signup(UserDto userDto) {
        userService.joinUser(userDto);
        return "redirect:/";
    }

    @GetMapping("/user/denied")
    public String denied() {
        return "/error/denied";
    }

    @ResponseBody
    @PostMapping("/user/check/mail")
    public int checkInvalidEmail(@RequestBody UserDto userDto) {
        return userService.checkInvalidEmail(userDto.getEmail());
    }
}
