package com.testmanage.entity;

import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CaseInfo implements Serializable {

    private Long id;
    private String case_id;
    private String case_name;
    private String case_pre;
    private String case_step;
    private String case_post;
    private String remark;
    private Boolean is_delete;
    private Boolean is_reviewed;
    private String create_user;
    private Date create_time;
    private String update_user;
    private Date update_time;
}
