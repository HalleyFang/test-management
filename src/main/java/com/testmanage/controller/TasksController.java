package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.testmanage.entity.Task;
import com.testmanage.service.TaskService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Task task = JsonParse.getGson().fromJson(params, Task.class);
        String s = params.get("startTime") == null ? "" :
                params.get("startTime").getAsString();
        String e = params.get("endTime") == null ? "" :
                params.get("endTime").getAsString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!s.isEmpty()) {
            task.setStart_date(simpleDateFormat.parse(s));
        }
        if (!e.isEmpty()) {
            task.setEnd_date(simpleDateFormat.parse(e));
        }
        taskService.addTask(task);
    }

    @PostMapping("listPage")
    public HttpServletResponse listPage(@RequestBody String body, HttpServletResponse resp) {
        String data = data(body);
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

    @PostMapping("listPageByUser")
    public HttpServletResponse listPageByUser(@RequestBody String body, HttpServletResponse resp) {
        String data = data(body);
        JsonArray json = new JsonArray();
        JsonArray jsonArray = JsonParse.getGson().fromJson(data, JsonArray.class);
        for (JsonElement jsonElement : jsonArray) {
            String executor = jsonElement.getAsJsonObject().get("executor").getAsString();
            if (executor.equalsIgnoreCase(UserContext.get().getUsername())) {
                json.add(jsonElement);
            }
        }
        data = JsonParse.getGson().toJson(json);
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

    @PostMapping("remove")
    public void removeTask(@RequestBody String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        Long id = params.get("id").getAsLong();
        taskService.deleteTask(id);
    }

    @PostMapping("edit")
    public void updateTask(@RequestBody String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        Task task = JsonParse.getGson().fromJson(params, Task.class);
        taskService.updateTask(task);
    }


    private String data(String body) {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject params = (JsonObject) bodyJson.get("params");
        String data = "";
        String label = params.get("label") == null ? null : params.get("label").getAsString();
        Integer page = params.get("page") == null || params.get("page").getAsString().isEmpty() ? null : params.get("page").getAsInt();
        if (label != null && !label.isEmpty()) {
            try {
                Long id = Long.parseLong(label);
                data = JsonParse.getGson().toJson(taskService.findTaskById(id));
            } catch (Exception e) {
                data = null;
            }
            if (data == null) {
                data = JsonParse.getGson().toJson(taskService.findTaskByLabel(label));
            }
        } else {
            if (page == null) {
                page = 1;
            }
            List<Task> ts = taskService.findTaskPage(page);
            data = JsonParse.getGson().toJson(ts);
        }
        return data;
    }
}
