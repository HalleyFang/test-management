package com.testmanage.service;

import com.testmanage.entity.CaseInfo;
import com.testmanage.mapper.CaseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseInfoService {

    @Autowired
    CaseInfoMapper caseInfoMapper;

    public void addCase(CaseInfo caseInfo){
        caseInfoMapper.insertCase(caseInfo);
    }

    public void updateCase(CaseInfo caseInfo){
        caseInfoMapper.updateCase(caseInfo);
    }

    public void deleteCase(CaseInfo caseInfo){
        caseInfoMapper.deleteCase(caseInfo);
    }

    public CaseInfo queryCase(String caseId){
        return caseInfoMapper.findByCaseId(caseId);
    }
}
