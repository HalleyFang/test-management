package com.testmanage.mapper;

import com.testmanage.entity.AutoCaseExec;
import com.testmanage.entity.ColumnChart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AutoCaseExecMapper {

    void add(AutoCaseExec autoCaseExec);

    void update(AutoCaseExec autoCaseExec);

    AutoCaseExec findCaseExecById(@Param("exec_id")Long exec_id,
                                  @Param("case_id")String case_id,
                                  @Param("exec_parameter")String exec_parameter);

    List<Long> findExecId(String is_v);

    List<ColumnChart> findColumnChart(@Param("is_v")String is_v,
                                      @Param("minId")Long minId,
                                      @Param("maxId")Long maxId);

    List<AutoCaseExec> findCaseExecByExecId(Long exec_id);

    List<AutoCaseExec> findCaseExecByCaseId(@Param("case_id")String case_id,
                                            @Param("exec_parameter")String exec_parameter);

    List<AutoCaseExec> findALL(String is_v);

}
