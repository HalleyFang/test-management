package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseInfo;
import com.testmanage.entity.ExcelCase;
import com.testmanage.service.CaseBodyToInfo;
import com.testmanage.service.CaseInfoService;
import com.testmanage.service.CaseTreeService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.ExcelUtils;
import com.testmanage.utils.JsonParse;
import com.testmanage.utils.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/case")
@Slf4j
public class CaseManagementController {

    @Autowired
    CaseInfoService caseInfoService;

    @Autowired
    CaseBodyToInfo caseBodyToInfo;

    @Autowired
    SequenceUtil sequenceUtil;

    @Autowired
    CaseTreeService caseTreeService;

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        Map<String, Object> map = caseTreeService.analysisRequest(body, "case");
        caseInfoService.addCase(map);
//        caseInfoService.addCase(caseBodyToInfo.caseBodyToInfo(body));
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void updateCase(@RequestBody String body, HttpServletResponse resp) throws Exception {
        JsonObject jsonObject = JsonParse.StringToJson(body);
        jsonObject.addProperty("case_step", jsonObject.get("case_step").getAsJsonArray().toString());
        CaseInfo caseInfo = JsonParse.getGson().fromJson(jsonObject, CaseInfo.class);
        caseInfoService.updateCase(caseInfo);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void deleteCase(@RequestParam String caseId, HttpServletResponse resp) throws Exception {
        caseInfoService.deleteCase(caseId);
        caseTreeService.deleteTree(caseId);
    }

    @RequestMapping(path = "/queryById", method = RequestMethod.POST)
    public HttpServletResponse queryCase(@RequestParam String caseId, HttpServletResponse resp) {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        CaseInfo caseInfo = caseInfoService.queryCase(caseId);
        if (caseInfo == null) {
            return null;
        }
        JsonObject data = JsonParse.StringToJson(JsonParse.getGson().toJson(caseInfo));
        JsonArray jsonArray = JsonParse.getGson().fromJson(caseInfo.getCase_step(), JsonArray.class);
        data.add("case_step", jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.toString().getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(path = "/queryByName", method = RequestMethod.POST)
    public HttpServletResponse queryCaseByName(@RequestParam String caseName, HttpServletResponse resp) {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        CaseInfo caseInfo = caseInfoService.queryCaseByName(caseName);
        if (caseInfo == null) {
            return null;
        }
        JsonObject data = JsonParse.StringToJson(JsonParse.getGson().toJson(caseInfo));
        JsonArray jsonArray = JsonParse.getGson().fromJson(caseInfo.getCase_step(), JsonArray.class);
        data.add("case_step", jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.toString().getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(path = "/importExcel", method = RequestMethod.POST)
    public void importExcel(@RequestBody MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelUtils<ExcelCase> excelUtil = new ExcelUtils<>(ExcelCase.class);
            List<ExcelCase> excelCases = excelUtil.importExcel(null, inputStream);
            List<CaseInfo> caseInfosInsert = new ArrayList<>();
            List<CaseInfo> caseInfosUpdate = new ArrayList<>();
            for (ExcelCase excelCase : excelCases) {
                CaseInfo caseInfo = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(excelCase), CaseInfo.class);
                caseInfo = caseBodyToInfo.caseSteps(caseInfo, excelCase.getCase_operate(), excelCase.getCase_expect());
                caseInfo.setIs_v(UserContext.get().getIsV());
                if (caseInfo.getCase_id() != null && !caseInfo.getCase_id().isEmpty()) {
                    caseInfo.setUpdate_user(UserContext.get().getUsername());
                    caseInfo.setUpdate_date(new Date());//时间会不准，需要研究一下怎么让数据库自动产生该时间
                    caseInfosUpdate.add(caseInfo);
                    continue;
                }
                caseInfo.setCase_id(caseInfoService.getCaseId());
                caseInfo.setId(sequenceUtil.getNext("caseInfo"));
                caseInfo.setCreate_user(UserContext.get().getUsername());
                caseInfo.setCreate_date(new Date());
                caseInfosInsert.add(caseInfo);
            }
            caseInfoService.addCase(caseInfosInsert);
            caseTreeService.addTree(caseInfosInsert);
            caseInfoService.updateCase(caseInfosUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
