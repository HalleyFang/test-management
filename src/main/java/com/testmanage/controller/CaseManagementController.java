package com.testmanage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/case")
@Slf4j
public class CaseManagementController {

    @RequestMapping(path = "add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addCase(@RequestBody String body, HttpServletResponse resp) {

    }


}
