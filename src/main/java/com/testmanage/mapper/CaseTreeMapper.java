package com.testmanage.mapper;

import com.testmanage.entity.CaseTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseTreeMapper {

    void addTree(CaseTreeNode caseTreeNode);

    void updateTree(CaseTreeNode caseTreeNode);

    List<CaseTreeNode> findTree(String is_v);

    List<CaseTreeNode> findTreeByParent(Long parent_id,String is_v);

    CaseTreeNode findTreeById(Long id);

    Long findNodeByName(String label,String is_v);

    Long findNodeByCaseId(String case_id);

}
