package com.testmanage.mapper;

import com.testmanage.entity.CaseInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseInfoMapper {

    void insertCase(CaseInfo info);

    void insertCaseBach(List<CaseInfo> caseInfos);

    void updateCase(CaseInfo info);

    void updateCaseBatch(List<CaseInfo> caseInfos);

    void deleteCase(CaseInfo info);

    void deleteCaseById(String case_id);


    CaseInfo findByCaseId(String case_id);

    CaseInfo findByCaseName(String case_name,String is_v);

    List<CaseInfo> findAll();

    List<CaseInfo> findByCreateUser();

}
