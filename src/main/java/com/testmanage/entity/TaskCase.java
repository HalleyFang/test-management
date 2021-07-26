package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskCase {

    private Long task_id;
    private String case_id;
    private Integer case_status = 0;
    private Long tree_id;
    private String update_user;
    private Date update_date;
}
