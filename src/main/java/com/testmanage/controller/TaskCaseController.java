package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.entity.TaskCase;
import com.testmanage.service.CaseTreeService;
import com.testmanage.service.TaskCaseService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import jodd.util.collection.MapEntry;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/taskCase")
public class TaskCaseController {

    @Autowired
    TaskCaseService taskCaseService;

    @Autowired
    CaseTreeService caseTreeService;

    @PostMapping("add")
    public void add(@RequestParam String taskId, @RequestBody String body, HttpServletResponse response){
        JsonObject bodyJson = JsonParse.StringToJson(body);
        String params = bodyJson.get("params").toString();
        String[] integers = params.substring(1,params.length()-1).split(",");
        Map<Long,String> caseIdMap = new HashMap<>();
        for(int i=0;i<integers.length;i++){
            CaseTreeNode node = caseTreeService.getTreeById(Long.valueOf(integers[i]));
            if(node.getCase_id()!=null && !node.getCase_id().isEmpty()){
                caseIdMap.put(node.getId(),node.getCase_id());
            }
        }
       taskCaseService.refreshCase(caseIdMap, Long.valueOf(taskId));
    }

}
