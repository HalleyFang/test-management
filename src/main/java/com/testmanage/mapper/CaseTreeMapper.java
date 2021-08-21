package com.testmanage.mapper;

import com.testmanage.entity.CaseTreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaseTreeMapper {

    void addTree(CaseTreeNode caseTreeNode);

    void updateTree(CaseTreeNode caseTreeNode);

    List<CaseTreeNode> findTree(String is_v);

    List<CaseTreeNode> findTreeByParent(@Param("parent_id") Long parent_id,
                                        @Param("is_v") String is_v);

    List<CaseTreeNode> findTreeByParentAndDir(@Param("parent_id") Long parent_id,
                                              @Param("is_dir") Boolean is_dir,
                                              @Param("is_v") String is_v);

    CaseTreeNode findTreeById(Long id);

    CaseTreeNode findTreeByCaseId(String case_id);

    Long findNodeByName(@Param("label") String label, @Param("is_v") String is_v);

    Long findNodeByCaseId(String case_id);

    void deleteTreeById(Long id);

}
