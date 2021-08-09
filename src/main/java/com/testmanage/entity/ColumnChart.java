package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ColumnChart {

    private Long exec_id;
    private Integer total;
    private Integer status;
    private Date min_start_date;
    private Date max_end_date;
}
