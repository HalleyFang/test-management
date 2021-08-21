package com.testmanage.mapper;

import com.testmanage.entity.TaskCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskCaseMapper {

    void addCase(TaskCase taskCase);

    void updateCase(TaskCase taskCase);

    void deleteCase(TaskCase taskCase);

    void deleteCaseById(String case_id);

    List<TaskCase> findByTaskId(Long task_id);

    TaskCase findById(@Param("task_id") Long task_id, @Param("case_id") String case_id);
}
