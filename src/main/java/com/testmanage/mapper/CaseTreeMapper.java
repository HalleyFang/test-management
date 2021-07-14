package com.testmanage.mapper;

import com.testmanage.entity.CaseTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseTreeMapper {

    void addTree(List<CaseTree> listTree);

    void updateTree(List<CaseTree> listTree);

    List<CaseTree> findTree();

    CaseTree findTreeById();
}
