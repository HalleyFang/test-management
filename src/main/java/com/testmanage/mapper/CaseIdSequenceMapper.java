package com.testmanage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CaseIdSequenceMapper {

    Integer getNext();

    void update(Integer id);
}
