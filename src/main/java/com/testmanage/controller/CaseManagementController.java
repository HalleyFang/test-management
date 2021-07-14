package com.testmanage.controller;

import com.testmanage.service.CaseInfoService;
import com.testmanage.service.CaseBodyToInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/case")
@Slf4j
public class CaseManagementController {

    @Autowired
    CaseInfoService caseInfoService;

    @Autowired
    CaseBodyToInfo caseBodyToInfo;

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        caseInfoService.addCase(caseBodyToInfo.caseBodyToInfo(body));
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void updateCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        caseInfoService.updateCase(caseBodyToInfo.caseBodyToInfo(body));
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void deleteCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        caseInfoService.deleteCase(caseBodyToInfo.caseBodyToInfo(body));
    }

    @RequestMapping(path = "/query", method = RequestMethod.GET)
    public void queryCase(@RequestParam String caseId, HttpServletResponse resp) {
        caseInfoService.queryCase(caseId);
    }


}
