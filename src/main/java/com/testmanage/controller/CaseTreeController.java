package com.testmanage.controller;

import com.testmanage.service.CaseTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/tree")
@Slf4j
public class CaseTreeController {

    @Autowired
    CaseTreeService caseTreeService;

    @RequestMapping( method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addTree(@RequestBody String body, HttpServletResponse resp){
        //todo 解析body
//        caseTreeService.addTree();
    }
}
