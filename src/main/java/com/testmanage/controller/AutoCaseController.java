package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.service.AutoCaseService;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class AutoCaseController {

    @Autowired
    AutoCaseService autoCaseService;

    @PostMapping("/addAutoCase")
    public void addAutoCase(@RequestBody String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        log.info("Receive request from autotest , caseId:" + bodyJson.get("caseId")
                + " , status:" + bodyJson.get("status")
                + " , execId:" + bodyJson.get("execId"));
        autoCaseService.addAndUpdateCase(bodyJson);
    }

    @GetMapping("/drawColumnChart")
    public HttpServletResponse drawColumnChart(HttpServletResponse response) {
        JsonObject jsonObject = autoCaseService.drawColumnChart();
        String data = JsonParse.JsonToString(jsonObject);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/drawScatterChart")
    public HttpServletResponse drawScatterChart(HttpServletResponse response) {
        JsonObject jsonObject = autoCaseService.drawScatterChart();
        String data = JsonParse.JsonToString(jsonObject);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/recentExec")
    public HttpServletResponse recentExec(HttpServletResponse response) {
        JsonArray jsonArray = autoCaseService.getRecentExec();
        String data = JsonParse.getGson().toJson(jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
