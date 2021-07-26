package com.testmanage.mapper;

import com.testmanage.entity.TaskCase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskCaseMapper {

    void addCase(TaskCase taskCase);

    void updateCase(TaskCase taskCase);

    void deleteCase(TaskCase taskCase);

    List<TaskCase> findByTaskId(Long task_id);

    TaskCase findById(Long task_id,String case_id);
}
