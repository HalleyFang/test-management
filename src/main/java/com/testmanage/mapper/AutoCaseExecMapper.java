package com.testmanage.mapper;

import com.testmanage.entity.AutoCaseExec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AutoCaseExecMapper {

    void add(AutoCaseExec autoCaseExec);

    void update(AutoCaseExec autoCaseExec);

    AutoCaseExec findCaseExecById(Long exec_id,String case_id,String exec_parameter);
}
