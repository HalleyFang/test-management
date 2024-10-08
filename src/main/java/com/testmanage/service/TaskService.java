package com.testmanage.service;

import com.testmanage.entity.Task;
import com.testmanage.entity.TaskCase;
import com.testmanage.mapper.TaskCaseMapper;
import com.testmanage.mapper.TaskMapper;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.SequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskCaseMapper taskCaseMapper;

    @Autowired
    SequenceUtil sequenceUtil;

    @CacheEvict("tasks")
    public void addTask(Task task) {
        task.setId(Long.valueOf(sequenceUtil.getNext("taskId")));
        task.setCreate_user(UserContext.get().getUsername());
        task.setIs_v(UserContext.get().getIsV());
        task.setCreate_date(new Date());
        taskMapper.addTask(task);
    }

    @CacheEvict("tasks")
    public void updateTask(Task task) {
        taskMapper.updateTaskById(task);
    }

    @CacheEvict("tasks")
    public void deleteTask(Long id) {
        taskMapper.deleteTaskById(id);
    }

    @Cacheable("tasks")
    public List<Task> findAllTask() {
        ;
        return taskMapper.findAllTask(UserContext.get().getIsV());
    }

    public Task findTaskById(Long id) throws ParseException {
        Task task = taskMapper.findTaskById(id);
        if (task == null) {
            return null;
        }
        Map<String, Integer> map = taskStatus(task.getId());
        task.setCase_count(map.get("caseCount"));
        task.setStatus(map.get("status"));
        task = formDate(task);
        return task;
    }

    private Task formDate(Task task) throws ParseException {
        Date startDate = task.getStart_date();
        Date endDate = task.getEnd_date();
        if (startDate != null) {
            task.setStart_date(startDate);
        }
        if (endDate != null) {
            task.setEnd_date(endDate);
        }
        return task;
    }

    public Task findTaskByLabel(String label) throws ParseException {
        Task task = taskMapper.findTaskByLabel(label, UserContext.get().getIsV());
        if (task == null) {
            return null;
        }
        Map<String, Integer> map = taskStatus(task.getId());
        task.setCase_count(map.get("caseCount"));
        task.setStatus(map.get("status"));
        task = formDate(task);
        return task;
    }

    public Map<String, Object> findTaskPage(Integer page, Integer pageSize, String executor, String taskStatus) throws ParseException {
        if (StringUtils.isEmpty(executor)) {
            executor = "all";
        }
        if (StringUtils.isEmpty(taskStatus)) {
            taskStatus = "all";
        }
        List<Task> ts = findAllTaskByExecutor(executor);
        List<Task> tst = findByTaskStatus(taskStatus, ts);
        return pageList(page, pageSize, tst);
    }


    public Map<String, Object> findUserTaskPage(Integer page, Integer pageSize, String taskStatus) throws ParseException {
        if (StringUtils.isEmpty(taskStatus)) {
            taskStatus = "all";
        }
        List<Task> tmpList = findAllTaskByExecutor(UserContext.get().getUsername());
        List<Task> tst = findByTaskStatus(taskStatus, tmpList);
        return pageList(page, pageSize, tst);
    }

    private List<Task> findAllTaskByExecutor(String executor) {
        List<Task> ts = findAllTask();
        if (executor.equalsIgnoreCase("all")) {
            return ts;
        }
        List<Task> tmpList = new ArrayList<>();
        for (Task t : ts) {
            if (!StringUtils.isEmpty(t.getExecutor())
                    && t.getExecutor().equalsIgnoreCase(executor)) {
                tmpList.add(t);
            }
        }
        return tmpList;
    }

    private List<Task> findByTaskStatus(String taskStatus, List<Task> tmpList) {
        for (Task task : tmpList) {
            Map<String, Integer> map = taskStatus(task.getId());
            task.setCase_count(map.get("caseCount"));
            task.setStatus(map.get("status"));
        }
        if (taskStatus.equalsIgnoreCase("all")) {
            return tmpList;
        }
        List<Task> ts = new ArrayList<>();
        if (taskStatus.equalsIgnoreCase("complete")) {
            for (Task t : tmpList) {
                if (t.getStatus() == 100) {
                    ts.add(t);
                }
            }
        } else {
            for (Task t : tmpList) {
                if (t.getStatus() != 100) {
                    ts.add(t);
                }
            }
        }
        return ts;
    }

    private Map<String, Object> pageList(Integer page, Integer pageSize, List<Task> tmpList) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", tmpList.size());
        List<Task> p = new ArrayList<>();
        for (int i = (page - 1) * pageSize; i < tmpList.size(); i++) {
            if (i < page * pageSize) {
                Task task = tmpList.get(i);
                task = formDate(task);
                p.add(task);
            } else {
                break;
            }
        }
        resultMap.put("taskList", p);
        return resultMap;
    }

    public Integer findTaskTotal(String type) throws ParseException {
        Integer total = 0;
        if (type != null) {
            switch (type.toLowerCase()) {
                case "user":
                    total = taskMapper.findAllUserTask(UserContext.get().getUsername(),
                            UserContext.get().getIsV()).size();
                    break;
                default:
                    total = taskMapper.findAllTask(UserContext.get().getIsV()).size();
                    break;
            }
        }
        return total;
    }

    public Map<String, Integer> taskStatus(Long taskId) {
        Map<String, Integer> map = new HashMap<>();
        List<TaskCase> list = taskCaseMapper.findByTaskId(taskId);
        map.put("caseCount", list.size());
        List<TaskCase> success = new ArrayList<>();
        for (TaskCase taskCase : list) {
            if (taskCase.getCase_status() == 1) {
                success.add(taskCase);
            }
        }
        Integer status = 0;
        if (list.size() > 0) {
            status = Math.round(((float) success.size() / list.size()) * 100);
        }
        map.put("status", status);
        return map;
    }
}
