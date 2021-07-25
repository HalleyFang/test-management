package com.testmanage.mapper;

import com.testmanage.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    void addTask(Task task);

    Task updateTaskById(Long id);

    void deleteTaskById(Long id);

    List<Task> findAllTask();

    Task findTaskById(Long id);

    Task findTaskByLabel(String label);
}
