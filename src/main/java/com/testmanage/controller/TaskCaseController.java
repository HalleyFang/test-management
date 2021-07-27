package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.entity.TaskCase;
import com.testmanage.service.CaseTreeService;
import com.testmanage.service.TaskCaseService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/taskCase")
public class TaskCaseController {

    @Autowired
    TaskCaseService taskCaseService;

    @Autowired
    CaseTreeService caseTreeService;

    @PostMapping("add")
    public void add(@RequestParam String taskId, @RequestBody String body, HttpServletResponse response) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        String params = bodyJson.get("params").toString();
        String[] integers = params.substring(1, params.length() - 1).split(",");
        Map<Long, String> caseIdMap = new HashMap<>();
        for (int i = 0; i < integers.length; i++) {
            CaseTreeNode node = caseTreeService.getTreeById(Long.valueOf(integers[i]));
            if (node.getCase_id() != null && !node.getCase_id().isEmpty()) {
                caseIdMap.put(node.getId(), node.getCase_id());
            }
        }
        taskCaseService.refreshCase(caseIdMap, Long.valueOf(taskId));
    }

    @GetMapping("queryStatus")
    public HttpServletResponse queryStatus(@RequestParam String taskId, @RequestParam String caseId, HttpServletResponse resp) {
        TaskCase ts = taskCaseService.query(Long.valueOf(taskId), caseId);
        if (ts == null) {
            return null;
        }
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        String data = JsonParse.getGson().toJson(ts);
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


    @PostMapping("update")
    public void update(@RequestBody String body, HttpServletResponse response) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = bodyJson.get("params").getAsJsonObject();
        Integer status = params.get("result").getAsJsonObject().get("status").getAsInt();
        Long taskId = params.get("taskId").getAsLong();
        String caseId = params.get("caseId").getAsString();
        TaskCase taskCase = new TaskCase();
        taskCase.setCase_id(caseId);
        taskCase.setTask_id(taskId);
        taskCase.setCase_status(status);
        taskCase.setUpdate_user(UserContext.get().getUsername());
        taskCase.setUpdate_date(new Date());
        taskCaseService.addCase(taskCase);
    }
}
