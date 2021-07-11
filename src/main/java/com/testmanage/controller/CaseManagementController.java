package com.testmanage.controller;

import com.testmanage.entity.CaseInfo;
import com.testmanage.service.CaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/case")
@Slf4j
public class CaseManagementController {

    @Autowired
    CaseInfoService caseInfoService;

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addCase(@RequestBody String body, HttpServletResponse resp) {
        //todo 解析body
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCase_id("");
        caseInfoService.addCase(caseInfo);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void updateCase(@RequestBody String body, HttpServletResponse resp) {
        //todo 解析body
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCase_id("");
        caseInfoService.updateCase(caseInfo);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void deleteCase(@RequestBody String body, HttpServletResponse resp) {
        //todo 解析body
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCase_id("");
        caseInfoService.deleteCase(caseInfo);
    }

    @RequestMapping(path = "/query", method = RequestMethod.GET)
    public void queryCase(@RequestParam String caseId, HttpServletResponse resp) {
        caseInfoService.queryCase(caseId);
    }


}
