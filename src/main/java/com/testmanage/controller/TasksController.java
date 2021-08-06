package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testmanage.entity.Task;
import com.testmanage.service.TaskService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("task")
public class TasksController {

    @Autowired
    TaskService taskService;

    @PostMapping("add")
    public void addTask(@RequestBody String body, HttpServletResponse response) throws ParseException {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        params = dateOp(params);
        Task task = JsonParse.getGson().fromJson(params, Task.class);
        task = taskDate(params,task);
        taskService.addTask(task);
    }

    private JsonObject dateOp(JsonObject params){
        String startDate = params.get("start_date").getAsString();
        String endDate = params.get("end_date").getAsString();
        if(startDate.isEmpty()){
            params.remove("start_date");
        }
        if(endDate.isEmpty()){
            params.remove("end_date");
        }
        return params;
    }


    private Task taskDate(JsonObject params,Task task) throws ParseException {
        String s = params.get("start_date") == null ? "" :
                params.get("start_date").getAsString();
        String e = params.get("end_date") == null ? "" :
                params.get("end_date").getAsString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!s.isEmpty()) {
            task.setStart_date(simpleDateFormat.parse(s));
        }
        if (!e.isEmpty()) {
            task.setEnd_date(simpleDateFormat.parse(e));
        }
        return task;
    }

    @PostMapping("listPage")
    public HttpServletResponse listPage(@RequestBody String body, HttpServletResponse resp) throws ParseException {
        String data = data(body,null);
        if(data == null || data.isEmpty()){
            return null;
        }
        JsonArray jsonArray = JsonParse.getGson().fromJson(data, JsonArray.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("array",jsonArray);
        Integer total = taskService.findTaskTotal("all");
        jsonObject.addProperty("total",total);
        String result = JsonParse.JsonToString(jsonObject);
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = result.getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("listPageByUser")
    public HttpServletResponse listPageByUser(@RequestBody String body, HttpServletResponse resp) throws ParseException {
        String type = "user";
        String data = data(body,type);
        if(data == null || data.isEmpty()){
            return null;
        }
        JsonArray jsonArray = JsonParse.getGson().fromJson(data, JsonArray.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("array",jsonArray);
        Integer total = taskService.findTaskTotal(type);
        jsonObject.addProperty("total",total);
        String result = JsonParse.JsonToString(jsonObject);
        OutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            byte[] dataByteArr = result.getBytes("UTF-8");
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

    @PostMapping("remove")
    public void removeTask(@RequestBody String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        Long id = params.get("id").getAsLong();
        taskService.deleteTask(id);
    }

    @PostMapping("edit")
    public void updateTask(@RequestBody String body) throws ParseException {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        params = dateOp(params);
        Task task = JsonParse.getGson().fromJson(params, Task.class);
        task = taskDate(params,task);
        taskService.updateTask(task);
    }


    private String data(String body,String type) throws ParseException {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        String data = "";
        String label = params.get("label") == null ? null : params.get("label").getAsString();
        Integer page = params.get("page") == null || params.get("page").getAsString().isEmpty() ? null : params.get("page").getAsInt();
        Integer pageSize = params.get("pageSize") == null || params.get("pageSize").getAsString().isEmpty() ? null : params.get("pageSize").getAsInt();
        if (label != null && !label.isEmpty()) {
            try {
                Long id = Long.parseLong(label);
                Task task = taskService.findTaskById(id);
                if(task!=null) {
                    data = JsonParse.getGson().toJson(task);
                    if(!data.startsWith("[")) {
                        data = "[" + data + "]";
                    }
                }
            } catch (Exception e) {
                data = null;
            }
            if (data == null || data.isEmpty()) {
                Task task = taskService.findTaskByLabel(label);
                if (task!=null) {
                    data = JsonParse.getGson().toJson(task);
                    if (!data.startsWith("[")) {
                        data = "[" + data + "]";
                    }
                }
            }
        } else {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null){
                pageSize = 10;
            }
            List<Task> ts;
             if(!StringUtils.isEmpty(type) && type.equalsIgnoreCase("user")) {
                 ts = taskService.findUserTaskPage(page,pageSize);
            }else {
                 ts = taskService.findTaskPage(page,pageSize);
             }
            data = JsonParse.getGson().toJson(ts);

        }
        return data;
    }

}
