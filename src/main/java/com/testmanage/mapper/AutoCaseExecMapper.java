package com.testmanage.mapper;

import com.testmanage.entity.AutoCaseExec;
import com.testmanage.entity.ColumnChart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AutoCaseExecMapper {

    void add(AutoCaseExec autoCaseExec);

    void update(AutoCaseExec autoCaseExec);

    AutoCaseExec findCaseExecById(Long exec_id,String case_id,String exec_parameter);

    List<Long> findExecId();

    List<ColumnChart> findColumnChart(String is_v,Long minId,Long maxId);

    List<AutoCaseExec> findCaseExecByExecId(Long exec_id);

    List<AutoCaseExec> findCaseExecByCaseId(String case_id,String exec_parameter);

    List<AutoCaseExec> findALL(String is_v);

}
