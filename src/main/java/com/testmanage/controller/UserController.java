package com.testmanage.controller;

import com.testmanage.service.user.UserConfService;
import com.testmanage.service.user.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    UserConfService userConfService;

    @GetMapping("/avatar")
    public String avatar(HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png");
        return "/assets/avatar.png";
    }

    @GetMapping("/userConf/getV")
    public String getV() {
        String value = userConfService.getV(UserContext.get().getUsername());
        return value;
    }

    @PostMapping("/userConf/setV")
    public boolean setV(@RequestParam String value) {
        userConfService.setV(UserContext.get().getUsername(),value);
        return true;
    }
}
