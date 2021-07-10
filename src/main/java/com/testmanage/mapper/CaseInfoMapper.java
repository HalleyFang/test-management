package com.testmanage.mapper;

import com.testmanage.entity.CaseInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseInfoMapper {

    void insertCase(CaseInfo info);

    void updateCase(CaseInfo info);

    void deleteCase(CaseInfo info);

    CaseInfo findByCaseId(String caseId);

    List<CaseInfo> findAll();

    List<CaseInfo> findByCreateUser();

}
