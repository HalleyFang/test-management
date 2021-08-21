package com.testmanage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceMapper {

    Integer getNext(String name);

    void update(@Param("name") String name, @Param("id") Integer id);
}
