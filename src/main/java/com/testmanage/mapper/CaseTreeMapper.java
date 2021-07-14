package com.testmanage.mapper;

import com.testmanage.entity.CaseTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseTreeMapper {

    void addTree(CaseTreeNode caseTreeNode);

    void updateTree(CaseTreeNode caseTreeNode);

    List<CaseTreeNode> findTree();

    List<CaseTreeNode> findTreeByParent(Long parentId);

    CaseTreeNode findTreeById(Long id);
}
