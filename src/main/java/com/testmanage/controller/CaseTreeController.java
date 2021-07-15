package com.testmanage.controller;

import com.testmanage.service.CaseTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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
        String data="{\"id\":1}";
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
        return resp;
    }
}
