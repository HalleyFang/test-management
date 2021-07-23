package com.testmanage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CaseTreeNode {

    private Long id;
    private String label;
    private String case_id;
    private Boolean is_dir = true;
    private Integer status;
    private Long parent_id;
    private Long pre_id;
    private Long post_id;
    private Boolean is_delete = false;
    private String create_user;
    private Date create_time;
    private String update_user;
    private Date update_time;
    private String is_v;
}
