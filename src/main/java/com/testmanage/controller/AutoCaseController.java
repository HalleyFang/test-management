package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.service.AutoCaseService;
import com.testmanage.utils.JsonParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

@RestController
public class AutoCaseController {

    @Autowired
    AutoCaseService autoCaseService;

    @PostMapping("/addAutoCase")
    public void addAutoCase(@RequestBody String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        autoCaseService.addAndUpdateCase(bodyJson);
    }


    @GetMapping("/drawColumnChart")
    public void drawColumnChart(HttpServletResponse response){

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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
