package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Task {

    private Long id;
    private String label;
    private String ms;
    private Boolean is_complete = false;
    private Integer status = 0;
    private Integer case_count;
    private String executor;
    private Date start_date;
    private Date end_date;
    private String create_user;
    private Date create_time;
    private String update_user;
    private Date update_time;
    private String is_v;

}
