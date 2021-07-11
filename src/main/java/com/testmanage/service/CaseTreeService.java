package com.testmanage.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTree;
import com.testmanage.mapper.CaseTreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CaseTreeService {

    @Autowired
    CaseTreeMapper caseTreeMapper;

    public synchronized void addTree(JsonObject treeJson) {
        List<CaseTree> listTreeBefore = caseTreeMapper.findTree();
        List<CaseTree> listTreeInsert = new ArrayList<>();
        List<CaseTree> listTreeUpdate = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : treeJson.entrySet()) {
            if (!listTreeBefore.contains(entry.getValue())) {
                //todo id
                CaseTree caseTree = caseTreeMapper.findTreeById();
                if (caseTree != null) {
                    //todo caseTree
                    listTreeUpdate.add(caseTree);
                } else {
                    listTreeInsert.add(caseTree);
                }
            }
        }
        if (listTreeInsert.size() > 0) {
            caseTreeMapper.addTree(listTreeInsert);
        }
        if (listTreeUpdate.size() > 0) {
            caseTreeMapper.updateTree(listTreeUpdate);
        }

    }
}
