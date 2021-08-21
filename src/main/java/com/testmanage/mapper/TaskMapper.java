package com.testmanage.mapper;

import com.testmanage.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    void addTask(Task task);

    void updateTaskById(Task task);

    void deleteTaskById(Long id);

    List<Task> findAllTask(String is_v);

    List<Task> findAllUserTask(@Param("executor") String executor, @Param("is_v") String is_v);

    Task findTaskById(Long id);

    Task findTaskByLabel(@Param("label") String label, @Param("is_v") String is_v);
}
