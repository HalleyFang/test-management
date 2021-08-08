package com.testmanage.mapper;

import com.testmanage.entity.AutoCase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AutoCaseMapper {

    void add(AutoCase autoCase);

    void update(AutoCase autoCase);

    AutoCase findCaseById(String case_id);

    Integer queryCaseCount(String is_v);
}
