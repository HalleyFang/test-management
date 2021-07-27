package com.testmanage.controller;

import com.testmanage.entity.TaskCase;
import com.testmanage.service.CaseTreeService;
import com.testmanage.service.TaskCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @GetMapping("taskTreeCase")
    public HttpServletResponse taskTreeCase(HttpServletResponse resp) {
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
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

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
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
