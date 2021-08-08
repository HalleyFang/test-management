package com.testmanage.mapper;

import com.testmanage.entity.AutoCaseExec;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AutoCaseExecMapper {

    void add(AutoCaseExec autoCaseExec);

    void update(AutoCaseExec autoCaseExec);

    AutoCaseExec findCaseExecById(Long exec_id,String case_id,String exec_parameter);

    List<Long> findExecId();

    List<AutoCaseExec> findCaseExecByExecId(Long exec_id);

    List<AutoCaseExec> findCaseExecByCaseId(String case_id,String exec_parameter);

    List<AutoCaseExec> findALL(String is_v);

}
