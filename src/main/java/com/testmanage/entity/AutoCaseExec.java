package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AutoCaseExec {

    private Long exec_id;
    private String case_id;
    private Integer status;
    private Date start_date;
    private Date end_date;
    private String exec_parameter = "";
    private Long current;
    private Integer exec_retry_count;
    private String remark;
    private Date update_date;
}
