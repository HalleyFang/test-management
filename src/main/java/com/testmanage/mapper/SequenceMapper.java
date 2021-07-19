package com.testmanage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceMapper {

    Integer getNext(String name);

    void update(String name,Integer id);
}
