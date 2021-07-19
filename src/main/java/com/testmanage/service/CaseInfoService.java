package com.testmanage.service;

import com.testmanage.entity.CaseInfo;
import com.testmanage.mapper.CaseInfoMapper;
import com.testmanage.utils.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CaseInfoService {

    @Autowired
    CaseInfoMapper caseInfoMapper;

    @Autowired
    SequenceUtil sequenceUtil;

    public void addCase(CaseInfo caseInfo){
        caseInfo.setCase_id(getCaseId());
        caseInfoMapper.insertCase(caseInfo);
    }

    public void addCase(List<CaseInfo> caseInfos){
        caseInfoMapper.insertCaseBach(caseInfos);
    }

    public void updateCase(CaseInfo caseInfo){
        caseInfoMapper.updateCase(caseInfo);
    }

    public void updateCase(List<CaseInfo> caseInfos){
        caseInfoMapper.updateCaseBatch(caseInfos);
    }


    public void deleteCase(CaseInfo caseInfo){
        caseInfoMapper.deleteCase(caseInfo);
    }

    public CaseInfo queryCase(String caseId){
        return caseInfoMapper.findByCaseId(caseId);
    }

    public String getCaseId() {
        Integer id = sequenceUtil.getNext("caseId");
        String pre = "";
        if (id < 10) {
            pre = "0000";
        } else if (id >= 10 && id < 100) {
            pre = "000";
        } else if (id >= 100 && id < 1000) {
            pre = "00";
        } else if (id >= 1000 && id < 10000) {
            pre = "0";
        } else {
            log.warn("there are too many cases !");
        }
        return "case-" + pre + id;
    }
}
