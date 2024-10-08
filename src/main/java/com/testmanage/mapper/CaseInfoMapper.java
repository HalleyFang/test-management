package com.testmanage.mapper;

import com.testmanage.entity.CaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaseInfoMapper {

    void insertCase(CaseInfo info);

    void insertCaseBatch(@Param("caseInfos") List<CaseInfo> caseInfos);

    void updateCase(CaseInfo info);

    void updateCaseBatch(@Param("caseInfos") List<CaseInfo> caseInfos);

    void deleteCase(CaseInfo info);

    void deleteCaseById(String case_id);


    CaseInfo findByCaseId(String case_id);

    CaseInfo findByCaseName(@Param("case_name") String case_name, @Param("is_v") String is_v);

    List<CaseInfo> findAll();

    List<CaseInfo> findByCreateUser();

    Integer findTotalCount(String is_v);

}
