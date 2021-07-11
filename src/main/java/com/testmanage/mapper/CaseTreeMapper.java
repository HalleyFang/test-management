package com.testmanage.mapper;

import com.testmanage.entity.CaseTree;

import java.util.List;

public interface CaseTreeMapper {

    void addTree(List<CaseTree> listTree);

    void updateTree(List<CaseTree> listTree);


    List<CaseTree> findTree();

    CaseTree findTreeById();
}
