package com.testmanage.service;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseInfo;
import com.testmanage.mapper.CaseInfoMapper;
import com.testmanage.utils.JsonParse;
import com.testmanage.utils.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CaseBodyToInfo {

    @Autowired
    CaseInfoMapper caseInfoMapper;

    @Autowired
    CaseInfoService caseInfoService;

    public CaseInfo caseBodyToInfo(String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        CaseInfo caseInfo = new CaseInfo();
        String case_id = caseInfoService.getCaseId();
        caseInfo.setCase_id(case_id);
        String case_name = bodyJson.get("case_name").getAsString();
        if (case_name.isEmpty()) {
            throw new Exception("case name can not be null");
        }
        caseInfo.setCase_name(case_name);
        String case_pre = bodyJson.get("case_pre").getAsString();
        caseInfo.setCase_pre(case_pre);
        String case_step = bodyJson.get("case_step").getAsString();
        caseInfo.setCase_step(case_step);
        String case_post = bodyJson.get("case_post").getAsString();
        caseInfo.setCase_post(case_post);
        String remark = bodyJson.get("remark").getAsString();
        caseInfo.setRemark(remark);
        String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
        if (caseInfoMapper.findByCaseId(case_id) instanceof CaseInfo) {
            caseInfo.setUpdate_user(currentUser);
        } else {
            caseInfo.setCreate_user(currentUser);
        }
        return caseInfo;
    }


}
