package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseInfo;
import com.testmanage.entity.ExcelCase;
import com.testmanage.entity.TaskCase;
import com.testmanage.service.*;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/tree")
@Slf4j
public class CaseTreeController {

    @Autowired
    CaseTreeService caseTreeService;

    @Autowired
    TaskCaseService taskCaseService;

    @Autowired
    CaseInfoService caseInfoService;

    @Autowired
    AutoCaseService autoCaseService;

    @Autowired
    CaseBodyToInfo caseBodyToInfo;

    @Autowired
    SequenceUtil sequenceUtil;

    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public HttpServletResponse getTree(HttpServletResponse resp) {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        String data = caseTreeService.getTree();
        if (data == null || data.isEmpty()) {
            return null;
        }
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addDir(@RequestBody String body, HttpServletResponse resp) throws Exception {
        Map<String, Object> map = caseTreeService.analysisRequest(body, "tree");
        caseTreeService.addTree(map);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void updateNode(@RequestBody String body, HttpServletResponse resp) throws Exception {
//        caseTreeService.updateTree();
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void deleteDir(@RequestParam Long id, HttpServletResponse resp) throws Exception {
        caseTreeService.deleteTree(id);
    }

    /**
     * 执行用例获取用例树
     *
     * @param taskId
     * @param resp
     * @return
     */
    @GetMapping("taskTreeCase")
    public HttpServletResponse taskTreeCase(@RequestParam String taskId, HttpServletResponse resp) throws Exception {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        List<TaskCase> ts = taskCaseService.findByTaskId(Long.valueOf(taskId));
        List<Long> treeId = new ArrayList<>();
        for (int i = 0; i < ts.size(); i++) {
            treeId.add(ts.get(i).getTree_id());
        }
        String data = caseTreeService.getTaskTree(Long.valueOf(taskId), treeId);
        if (data == null || data.isEmpty()) {
            return null;
        }
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.getBytes("UTF-8");
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

    /**
     * 获取选中的节点
     *
     * @param taskId
     * @param resp
     * @return
     */
    @GetMapping("taskCaseChecked")
    public HttpServletResponse taskCase(@RequestParam String taskId, HttpServletResponse resp) {
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        List<TaskCase> ts = taskCaseService.findByTaskId(Long.valueOf(taskId));
        StringBuffer checkedKeys = new StringBuffer();
        checkedKeys.append("[");
        for (int i = 0; i < ts.size(); i++) {
            if (i == ts.size() - 1) {
                checkedKeys.append(ts.get(i).getTree_id());
                break;
            }
            checkedKeys.append(ts.get(i).getTree_id()).append(",");
        }
        checkedKeys.append("]");
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = checkedKeys.toString().getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/caseTotal")
    public HttpServletResponse caseTotal(HttpServletResponse response) {
        response.setHeader("content-type", "application/json;charset=UTF-8");
        JsonObject jsonObject = new JsonObject();
        Integer total = caseInfoService.queryCaseTotal();
        Double autoCount = Double.valueOf(autoCaseService.queryCaseCount());
        Double auto = 0.00;
        if (total != 0) {
            BigDecimal bigDecimal = new BigDecimal((autoCount / total) * 100);
            auto = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        jsonObject.addProperty("total", total);
        jsonObject.addProperty("auto", auto);
        String data = JsonParse.JsonToString(jsonObject);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] dataByteArr = data.getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(path = "/importExcel", method = RequestMethod.POST)
    public void importExcel(@RequestBody MultipartFile file, HttpServletResponse response) throws IOException {
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
            if (caseInfosInsert.size() > 0) {
                caseInfoService.addCase(caseInfosInsert);
                caseTreeService.addTree(caseInfosInsert);
            }
            if (caseInfosUpdate.size() > 0) {
                caseInfoService.updateCase(caseInfosUpdate);
                caseTreeService.updateTreeLabel(caseInfosUpdate);
            }
        } catch (Exception e) {
            response.sendError(500);
        }

    }
}
