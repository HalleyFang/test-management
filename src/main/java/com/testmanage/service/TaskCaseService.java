package com.testmanage.service;

import com.testmanage.entity.TaskCase;
import com.testmanage.mapper.TaskCaseMapper;
import com.testmanage.service.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskCaseService {

    @Autowired
    TaskCaseMapper taskCaseMapper;

    public List<TaskCase> findByTaskId(Long id) {
        return taskCaseMapper.findByTaskId(id);
    }

    public void addCase(TaskCase taskCase) {
        TaskCase c = taskCaseMapper.findById(taskCase.getTask_id(), taskCase.getCase_id());
        if (c == null) {
            taskCaseMapper.addCase(taskCase);
        }
    }

    public void updateCase(TaskCase taskCase) {
        TaskCase c = taskCaseMapper.findById(taskCase.getTask_id(), taskCase.getCase_id());
        if (c != null) {
            if (taskCase.getCase_status() != c.getCase_status()) {
                taskCaseMapper.updateCase(taskCase);
            }
        } else {
            taskCaseMapper.addCase(taskCase);
        }
    }

    public TaskCase query(Long taskId, String caseId) {
        return taskCaseMapper.findById(taskId, caseId);
    }


    public void refreshCase(Map<Long, String> caseIdMap, Long taskId) {
        for (Map.Entry entry : caseIdMap.entrySet()) {
            TaskCase taskCase = new TaskCase();
            taskCase.setCase_id(entry.getValue().toString());
            taskCase.setTask_id(Long.valueOf(taskId));
            taskCase.setTree_id((Long) entry.getKey());
            taskCase.setUpdate_user(UserContext.get().getUsername());
            taskCase.setUpdate_date(new Date());
            addCase(taskCase);
        }
        List<TaskCase> ts = taskCaseMapper.findByTaskId(taskId);
        for (TaskCase t : ts) {
            if (!caseIdMap.containsKey(t.getTree_id())) {
                taskCaseMapper.deleteCase(t);
            }
        }
    }

    public void deleteCaseById(String case_id) {
        taskCaseMapper.deleteCaseById(case_id);
    }
}
