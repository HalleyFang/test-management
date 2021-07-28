package com.testmanage.service;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseInfo;
import com.testmanage.mapper.CaseInfoMapper;
import com.testmanage.utils.JsonParse;
import jodd.util.collection.MapEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CaseBodyToInfo {

    @Autowired
    CaseInfoMapper caseInfoMapper;

    @Autowired
    CaseInfoService caseInfoService;

    public CaseInfo caseBodyToInfo(String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        if (bodyJson == null) {
            return null;
        }
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


    public CaseInfo caseSteps(CaseInfo caseInfo, String operate, String expect) throws Exception {
        Map<Integer, String> opMap = opStr(operate);
        Map<Integer, String> expMap = opStr(expect);
        if (expMap.size() > opMap.size()) {
            throw new Exception("预期结果多于步骤，解析失败");
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (Map.Entry<Integer, String> entry : opMap.entrySet()) {
            stringBuffer.append("{\"step\":\"").append(entry.getValue()).append("\",\"expect\":\"");
            if (expMap.get(entry.getKey())!=null && !expMap.get(entry.getKey()).isEmpty()) {
                stringBuffer.append(expMap.get(entry.getKey()));
            }
            stringBuffer.append("\"},");
        }
        stringBuffer.substring(0, stringBuffer.lastIndexOf(","));
        stringBuffer.append("]");
        caseInfo.setCase_step(stringBuffer.toString());
        return caseInfo;
    }


    private Map<Integer, String> opStr(String str) {
        String[] strings = str.split("\n");
        List<String> list = new ArrayList<>();
        for (int i=0;i<strings.length;i++) {
            String s = strings[i];
            if(i==0 && !s.matches("^[0-9]+[.].*")){
                list.add(str);
                break;
            }
            if (s.matches("^[0-9]+[.].*")) {
                list.add(s);
            } else {
                String t = list.get(i - 1);
                t = t + "\n" + s;
//                list.remove(list.size() - 1);
                list.add(t);
            }
        }
        Map<Integer, String> map = new LinkedHashMap<>();
        for (String ss : list) {
            int index = ss.indexOf(".");
            map.put(Integer.valueOf(ss.substring(0, index)), ss.substring(index + 1));
        }
        return map;
    }
}
