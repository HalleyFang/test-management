package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AutoCase {

    private String case_id;
    private Integer type;
    private String content;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;

}
