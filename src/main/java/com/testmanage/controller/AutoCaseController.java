package com.testmanage.controller;

import com.google.gson.JsonObject;
import com.testmanage.service.AutoCaseService;
import com.testmanage.utils.JsonParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoCaseController {

    @Autowired
    AutoCaseService autoCaseService;

    @PostMapping("/addAutoCase")
    public void addAutoCase(@RequestBody String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        autoCaseService.addAndUpdateCase(bodyJson);
    }


}
