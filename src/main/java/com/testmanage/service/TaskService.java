package com.testmanage.service;

import com.testmanage.entity.Task;
import com.testmanage.entity.TaskCase;
import com.testmanage.mapper.TaskCaseMapper;
import com.testmanage.mapper.TaskMapper;
import com.testmanage.mapper.UserConfMapper;
import com.testmanage.service.user.UserContext;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskCaseMapper taskCaseMapper;

    @CacheEvict("tasks")
    public void addTask(Task task){
        task.setCreate_user(UserContext.get().getUsername());
        task.setIs_v(UserContext.get().getIsV());
        task.setCreate_time(new Date());
        taskMapper.addTask(task);
    }

    @CacheEvict("tasks")
    public void updateTask(Task task){
        taskMapper.updateTaskById(task);
    }

    @CacheEvict("tasks")
    public void deleteTask(Long id){
        taskMapper.deleteTaskById(id);
    }

    @Cacheable("tasks")
    public List<Task> findAllTask(){ ;
        return taskMapper.findAllTask(UserContext.get().getIsV());
    }

    public Task findTaskById(Long id){
        Task task = taskMapper.findTaskById(id);
        Map<String,Integer> map = taskStatus(task.getId());
        task.setCase_count(map.get("caseCount"));
        task.setStatus(map.get("status"));
        return task;
    }

    public Task findTaskByLabel(String label){
        Task task = taskMapper.findTaskByLabel(label,UserContext.get().getIsV());
        Map<String,Integer> map = taskStatus(task.getId());
        task.setCase_count(map.get("caseCount"));
        task.setStatus(map.get("status"));
        return task;
    }

    public List<Task> findTaskPage(Integer page){
        List<Task> p = new ArrayList<>();
        List<Task> ts = findAllTask();
            for(int i=(page-1)*20;i<ts.size();i++){
                if(i<page*20){
                    Task task = ts.get(i);
                    Map<String,Integer> map = taskStatus(task.getId());
                    task.setCase_count(map.get("caseCount"));
                    task.setStatus(map.get("status"));
                    p.add(task);
                }else {
                    break;
                }
            }
        return p;
    }

    public Map<String,Integer> taskStatus(Long taskId){
        Map<String,Integer> map= new HashMap<>();
        List<TaskCase> list = taskCaseMapper.findByTaskId(taskId);
        map.put("caseCount",list.size());
        List<TaskCase> success = new ArrayList<>();
        for(TaskCase taskCase:list){
            if (taskCase.getCase_status()==1){
                success.add(taskCase);
            }
        }
        Integer status = 0;
        if(list.size()>0){
            status = Math.round(((float) success.size()/list.size())*100);
        }
        map.put("status",status);
        return map;
    }
}
