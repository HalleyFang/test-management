package com.testmanage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @GetMapping("/avatar")
    public String avatar(HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png");
        return "/assets/avatar.png";
    }
}
