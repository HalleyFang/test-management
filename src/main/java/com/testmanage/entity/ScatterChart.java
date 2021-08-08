package com.testmanage.entity;

import lombok.Data;

@Data
public class ScatterChart {

    private Long exec_id;
    private String case_id;
    private String exec_parameter;
    private Long exec_time;
    private Double failed_rate;
    private Integer exec_total;
    private Integer failed_count;
    private String is_v;
}
