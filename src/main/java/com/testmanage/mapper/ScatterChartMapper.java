package com.testmanage.mapper;

import com.testmanage.entity.ScatterChart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScatterChartMapper {

    List<ScatterChart> findByExecId(@Param("exec_id") Long exec_id,
                                    @Param("is_v") String is_v);

    List<Long> findExecId(String is_v);

    List<ScatterChart> findByCaseId(@Param("case_id") String case_id,
                                    @Param("exec_parameter") String exec_parameter,
                                    @Param("is_v") String is_v);

    ScatterChart findOneByCaseId(@Param("case_id") String case_id,
                                 @Param("is_v") String is_v);

    void batchInsert(List<ScatterChart> scatterCharts);

}
