package com.testmanage.mapper;

import com.testmanage.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    void addTask(Task task);

    void updateTaskById(Task task);

    void deleteTaskById(Long id);

    List<Task> findAllTask(String is_v);

    Task findTaskById(Long id);

    Task findTaskByLabel(String label,String is_v);
}
