package com.testmanage.mapper;

import com.testmanage.entity.ScatterChart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScatterChartMapper {

    List<ScatterChart> findByExecId(Long exec_id,String is_v);

    List<Long> findExecId(String is_v);

    List<ScatterChart> findByCaseId(String case_id,String exec_parameter,String is_v);

    ScatterChart findOneByCaseId(String case_id,String is_v);

    void batchInsert(List<ScatterChart> scatterCharts);

}
