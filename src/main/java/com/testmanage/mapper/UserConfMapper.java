package com.testmanage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserConfMapper {

    String getParameter(String username,String parameter);

    void setParameter(String username,String parameter,String value);

    Integer getCount(String username,String parameter);
}
