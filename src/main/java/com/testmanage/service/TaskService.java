package com.testmanage.service;

import com.testmanage.entity.Task;
import com.testmanage.mapper.TaskMapper;
import com.testmanage.service.user.UserContext;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskMapper taskMapper;

    @CacheEvict("tasks")
    public void addTask(Task task){
        task.setCreate_user(UserContext.get().getUsername());
        task.setIs_v(UserContext.get().getIsV());
        task.setCreate_time(new Date());
        taskMapper.addTask(task);
    }

    @CacheEvict("tasks")
    public void updateTask(){

    }

    @Cacheable("tasks")
    public List<Task> findAllTask(){
        return taskMapper.findAllTask();
    }

    public Task findTaskById(Long id){
        return taskMapper.findTaskById(id);
    }

    public Task findTaskByLabel(String label){
        return taskMapper.findTaskByLabel(label);
    }

    public List<Task> findTaskPage(Integer page){
        List<Task> p = new ArrayList<>();
        List<Task> ts = findAllTask();
        if(ts.size()<20){
            p = ts;
        }else {
            for(int i=(page-1)*20;i<ts.size();i++){
                if(i<page*20){
                    p.add(ts.get(i));
                }else {
                    break;
                }
            }
        }
        return p;
    }
}
