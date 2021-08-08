package com.testmanage.mapper;

import com.testmanage.entity.ScatterChart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScatterChartMapper {

    List<ScatterChart> findByExecId(Long exec_id);

    List<Long> findExecId();

    List<ScatterChart> findByCaseId(String case_id,String exec_parameter);

    ScatterChart findOneByCaseId(String case_id);

    void batchInsert(List<ScatterChart> scatterCharts);

}
