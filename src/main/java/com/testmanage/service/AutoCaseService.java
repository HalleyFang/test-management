package com.testmanage.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.testmanage.entity.AutoCase;
import com.testmanage.entity.AutoCaseExec;
import com.testmanage.mapper.AutoCaseExecMapper;
import com.testmanage.mapper.AutoCaseMapper;
import com.testmanage.utils.JsonParse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AutoCaseService {

    @Autowired
    AutoCaseMapper autoCaseMapper;

    @Autowired
    AutoCaseExecMapper autoCaseExecMapper;

    public synchronized void addAndUpdateCase(JsonObject bodyJson) {
        AutoCase autoCase = addAuto(bodyJson);
        String case_id = autoCase.getCase_id();
        AutoCase currentCase = autoCaseMapper.findCaseById(case_id);
        if (currentCase instanceof AutoCase) {
            //update
            if (!JsonParse.getGson().toJson(autoCase)
                    .equalsIgnoreCase(JsonParse.getGson().toJson(currentCase))) {
                autoCaseMapper.update(autoCase);
            }
        } else {
            autoCaseMapper.add(autoCase);
        }
        AutoCaseExec autoCaseExec = addExec(bodyJson);
        AutoCaseExec currentCaseExec = autoCaseExecMapper
                .findCaseExecById(autoCaseExec.getExec_id(), autoCaseExec.getCase_id(), autoCaseExec.getExec_parameter());
        if (currentCaseExec instanceof AutoCaseExec) {
            //update
            if (!JsonParse.getGson().toJson(autoCaseExec)
                    .equalsIgnoreCase(JsonParse.getGson().toJson(currentCaseExec))) {
                if( currentCaseExec.getCurrent() > autoCaseExec.getCurrent() &&
                        autoCaseExec.getStatus()==5 &&
                        currentCaseExec.getStart_date()==null){
                    autoCaseExec.setStatus(null);
                }
                autoCaseExec.setUpdate_date(new Date());
                autoCaseExecMapper.update(autoCaseExec);
            }
        } else {
            autoCaseExecMapper.add(autoCaseExec);
        }
    }

    public void addCase(AutoCase autoCase) {
        autoCaseMapper.add(autoCase);
    }

    public AutoCase findCaseById(String caseId) {
        return autoCaseMapper.findCaseById(caseId);
    }

    private AutoCase addAuto(JsonObject bodyJson) {
        JsonObject autoCaseJson = new JsonObject();
        autoCaseJson.addProperty("case_id", bodyJson.get("caseId").getAsString());
        autoCaseJson.addProperty("type", bodyJson.get("type").getAsString());
        autoCaseJson.addProperty("create_user", bodyJson.get("author").getAsString());
        autoCaseJson.addProperty("create_date", bodyJson.get("createDate").getAsString());
        return JsonParse.getGson().fromJson(autoCaseJson, AutoCase.class);
    }

    private AutoCaseExec addExec(JsonObject bodyJson) {
        JsonObject autoCaseJson = new JsonObject();
        autoCaseJson.addProperty("exec_id", bodyJson.get("execId").getAsString());
        autoCaseJson.addProperty("current", bodyJson.get("currentTime").getAsString());
        autoCaseJson.addProperty("case_id", bodyJson.get("caseId").getAsString());
        autoCaseJson.addProperty("status", bodyJson.get("status").getAsInt());
        String star_date = bodyJson.get("startDate") == null || bodyJson.get("startDate") instanceof JsonNull
                ? null : bodyJson.get("startDate").getAsString();
        if (!StringUtils.isEmpty(star_date)) {
            autoCaseJson.addProperty("start_date", star_date);
        }
        String end_date = bodyJson.get("endDate") == null || bodyJson.get("endDate") instanceof JsonNull
                ? null : bodyJson.get("endDate").getAsString();
        if (!StringUtils.isEmpty(end_date)) {
            autoCaseJson.addProperty("end_date", end_date);
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return gson.fromJson(autoCaseJson, AutoCaseExec.class);
    }

}
