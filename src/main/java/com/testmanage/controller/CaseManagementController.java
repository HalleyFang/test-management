package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseInfo;
import com.testmanage.service.CaseBodyToInfo;
import com.testmanage.service.CaseInfoService;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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
        JsonObject jsonObject = JsonParse.StringToJson(body);
        jsonObject.addProperty("case_step",jsonObject.get("case_step").getAsJsonArray().toString());
        CaseInfo caseInfo = JsonParse.getGson().fromJson(jsonObject,CaseInfo.class);
        caseInfoService.updateCase(caseInfo);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void deleteCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        caseInfoService.deleteCase(caseBodyToInfo.caseBodyToInfo(body));
    }

    @RequestMapping(path = "/query", method = RequestMethod.POST)
    public HttpServletResponse queryCase(@RequestParam String caseId, HttpServletResponse resp) {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        CaseInfo caseInfo = caseInfoService.queryCase(caseId);
        JsonObject data = JsonParse.StringToJson(JsonParse.getGson().toJson(caseInfo));
        JsonArray jsonArray = JsonParse.getGson().fromJson(caseInfo.getCase_step(),JsonArray.class);
        data.add("case_step",jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.toString().getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


}
