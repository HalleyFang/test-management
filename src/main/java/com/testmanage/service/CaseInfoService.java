package com.testmanage.service;

import com.testmanage.entity.CaseInfo;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.mapper.CaseInfoMapper;
import com.testmanage.service.user.UserConfService;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import com.testmanage.utils.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CaseInfoService {

    @Autowired
    CaseInfoMapper caseInfoMapper;

    @Autowired
    SequenceUtil sequenceUtil;

    @Autowired
    CaseTreeService caseTreeService;

    public void addCase(CaseInfo caseInfo) {
        caseInfo.setCase_id(getCaseId());
        caseInfo.setIs_v(UserContext.get().getIsV());
        caseInfo.setCreate_user(UserContext.get().getUsername());
        caseInfo.setCreate_date(new Date());
        caseInfoMapper.insertCase(caseInfo);
    }

    public synchronized void addCase(Map<String, Object> map) throws Exception {
        CaseTreeNode node = (CaseTreeNode) map.get("currentNode");
        String caseId = node.getCase_id();
        if (caseId != null) {
            throw new Exception("用例不合法");
        }
        caseId = getCaseId();
        node.setCase_id(caseId);
        map.put("currentNode", node);
        caseTreeService.addTree(map);
        log.info("insert tree label " + node.getLabel());
        CaseInfo caseInfo = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(node), CaseInfo.class);
        caseInfo.setCase_id(caseId);
        caseInfo.setCase_name(node.getLabel());
        caseInfo.setCase_step("[{\"step\":\"\",\"expect\":\"\"}]");
        caseInfo.setIs_v(UserContext.get().getIsV());
        caseInfo.setCreate_user(UserContext.get().getUsername());
        caseInfo.setCreate_date(new Date());
        caseInfoMapper.insertCase(caseInfo);
        log.info("insert case id " + caseId);
    }

    public void addCase(List<CaseInfo> caseInfos) {
        if (caseInfos.size()>0) {
            caseInfoMapper.insertCaseBatch(caseInfos);
        }
    }

    public void updateCase(CaseInfo caseInfo) throws Exception {
        if(caseInfo.getUpdate_user().equalsIgnoreCase("Automation")) {
            log.info("will update case " + caseInfo.getCase_id() + " by Automation");
        }else {
            caseInfo.setUpdate_user(UserContext.get().getUsername());
            log.info("will update case " + caseInfo.getCase_id() + " by " + UserContext.get().getUsername());
        }
        caseInfo.setUpdate_date(new Date());
        caseInfoMapper.updateCase(caseInfo);
        log.info("update case " + caseInfo.getCase_id() + " success");
        CaseTreeNode node = caseTreeService.getTreeByCaseId(caseInfo.getCase_id());
        if(node == null){
            throw new Exception("更新case对应的tree节点未找到");
        }
        String treeLabel = node.getLabel();
        if((treeLabel ==null || !treeLabel.equalsIgnoreCase(caseInfo.getCase_name()))
        && caseInfo.getCase_name() != null){
            CaseTreeNode caseTreeNode = new CaseTreeNode();
            caseTreeNode.setId(node.getId());
            caseTreeNode.setLabel(caseInfo.getCase_name());
            caseTreeNode.setUpdate_user(UserContext.get().getUsername());
            caseTreeNode.setUpdate_date(new Date());
            caseTreeService.updateTree(caseTreeNode);
        }
    }

    public void updateCase(List<CaseInfo> caseInfos) {
        caseInfoMapper.updateCaseBatch(caseInfos);
    }


    public void deleteCase(CaseInfo caseInfo) {
        caseInfoMapper.deleteCase(caseInfo);
    }

    public void deleteCase(String caseId) {
        caseInfoMapper.deleteCaseById(caseId);
    }

    public CaseInfo queryCase(String caseId) {
        return caseInfoMapper.findByCaseId(caseId);
    }

    public Integer queryCaseTotal() {
        return caseInfoMapper.findTotalCount(UserContext.get().getIsV());
    }


    public CaseInfo queryCaseByName(String caseName) {
        return caseInfoMapper.findByCaseName(caseName, UserContext.get().getIsV());
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
