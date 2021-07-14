package com.testmanage.mapper;

import com.testmanage.entity.CaseTreeNode;

import java.util.List;

public interface CaseTreeMapper {

    void addTree(CaseTreeNode caseTreeNode);

    void updateTree(CaseTreeNode caseTreeNode);


    List<CaseTreeNode> findTree();

    CaseTreeNode findTreeById(Long id);
}
