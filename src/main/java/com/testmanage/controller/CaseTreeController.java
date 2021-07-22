package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.service.CaseTreeService;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping(path = "/tree")
@Slf4j
public class CaseTreeController {

    @Autowired
    CaseTreeService caseTreeService;

    @RequestMapping( method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public HttpServletResponse getTree(HttpServletResponse resp){
        resp.setHeader("content-type", "application/json;charset=UTF-8");
        String data=caseTreeService.getTree();
        if(data == null || data.isEmpty()){
            return null;
        }
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = data.getBytes("UTF-8");
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

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public void addDir(@RequestBody String body, HttpServletResponse resp) throws Exception {
        Map<String,Object> map = caseTreeService.analysisRequest(body);
        caseTreeService.addTree(map);
    }
}
