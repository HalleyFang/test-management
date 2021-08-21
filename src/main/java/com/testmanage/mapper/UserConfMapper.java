package com.testmanage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserConfMapper {

    String getParameter(@Param("username") String username, @Param("parameter") String parameter);

    void setParameter(@Param("username") String username, @Param("parameter") String parameter, @Param("value") String value);

    Integer getCount(@Param("username") String username, @Param("parameter") String parameter);
}
