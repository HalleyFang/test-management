package com.testmanage.entity;

import com.testmanage.annotation.ExcelInfo;
import lombok.Data;

@Data
public class ExcelCase {

    @ExcelInfo(name = "模块名称",column = "模块名称")
    private String parent_name;
    @ExcelInfo(name = "用例标题",column = "用例标题")
    private String case_name;
    @ExcelInfo(name = "用例编号",column = "用例编号")
    private String case_id;
    @ExcelInfo(name = "前置条件",column = "前置条件")
    private String case_pre;
    @ExcelInfo(name = "用例步骤",column = "用例步骤")
    private String case_operate;
    @ExcelInfo(name = "预期结果",column = "预期结果")
    private String case_expect;
    @ExcelInfo(name = "后置步骤",column = "后置步骤")
    private String case_post;
    @ExcelInfo(name = "备注",column = "备注")
    private String remark;
}
