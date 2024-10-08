package com.testmanage.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CaseInfo implements Serializable {

    private Integer id;
    private String parent_name;
    private String case_id;
    private String case_name;
    private String case_pre;
    private String case_step;
    private String case_post;
    private String remark;
    private Boolean is_delete = false;
    private Boolean is_reviewed = false;
    private String issue_id;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String is_v;
    private Boolean is_auto = false;
}
