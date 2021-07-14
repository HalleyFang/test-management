package com.testmanage.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.mapper.CaseTreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class CaseTreeService {

    @Autowired
    CaseTreeMapper caseTreeMapper;

    @CacheEvict(value = "tree")
    public synchronized void addTree(JsonObject treeJson) throws Exception {
        Map<String,Object> resultMap = analysisTreeJson(treeJson);
        if (!idIsExist((Long) resultMap.get("id"))) {
            updateTree(treeJson);
        } else {
            CaseTreeNode currentNode = (CaseTreeNode) resultMap.get("currentNode");
            if(idIsExist((Long) resultMap.get("preId"))){
                currentNode.setPre_id((Long) resultMap.get("preId"));
            }
            if(idIsExist((Long) resultMap.get("postId"))){
                currentNode.setPost_id((Long) resultMap.get("postId"));
            }
            caseTreeMapper.addTree(currentNode);
        }

    }

    @CacheEvict(value = "tree")
    public synchronized void updateTree(JsonObject treeJson) throws Exception {
        Map<String,Object> resultMap = analysisTreeJson(treeJson);
        CaseTreeNode currentNode = (CaseTreeNode) resultMap.get("currentNode");
        if (!idIsExist((Long) resultMap.get("id"))) {
            addTree(treeJson);
        } else {
            CaseTreeNode treeNode = caseTreeMapper.findTreeById((Long) resultMap.get("id"));
            if(treeNode.getPre_id() == resultMap.get("preId")) {
                caseTreeMapper.updateTree(currentNode);//todo 没变的字段不更新
            }else {
                if(treeNode.getPost_id() == resultMap.get("postId")){
                    throw new Exception("node is invalid");
                }else {
                    CaseTreeNode oldPreNode = caseTreeMapper.findTreeById(treeNode.getPre_id());
                    CaseTreeNode oldPostNode = caseTreeMapper.findTreeById(treeNode.getPost_id());
                    oldPreNode.setPost_id(treeNode.getPost_id());
                    oldPostNode.setPre_id(treeNode.getPre_id());
                    caseTreeMapper.updateTree(oldPreNode);
                    caseTreeMapper.updateTree(oldPostNode);
                    currentNode.setPre_id((Long) resultMap.get("preId"));
                    currentNode.setPost_id((Long) resultMap.get("postId"));
                    caseTreeMapper.updateTree(currentNode);
                }
            }
        }
    }

    private Map<String, Object> analysisTreeJson(JsonObject treeJson) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        JsonObject currentJson = treeJson.get("currentNode").getAsJsonObject();
        if(currentJson == null){
            throw new Exception("node is not invalid");
        }
        JsonObject preJson = treeJson.get("currentNode").getAsJsonObject();
        JsonObject postJson = treeJson.get("currentNode").getAsJsonObject();
        Long preId = -1L;
        Long postId = -1L;
        if(preJson != null) {
            preId = preJson.get("id").getAsLong();
            if(!idIsExist(preId)){
                throw new Exception("pre node is not invalid");
            }
        }
        if(postJson != null) {
            postId = postJson.get("id").getAsLong();
            if(!idIsExist(postId)){
                throw new Exception("post node is not invalid");
            }
        }
        if(!idIsExist(preId) && !idIsExist(postId)){
            throw new Exception("pre and post node are not invalid");
        }

        Long id = currentJson.get("id").getAsLong();
        resultMap.put("id",id);
        resultMap.put("currentNode",jsonToNode(currentJson));
        resultMap.put("preId",preId);
        resultMap.put("preNode",jsonToNode(preJson));
        resultMap.put("postId",postId);
        resultMap.put("postNode",jsonToNode(postJson));
        return resultMap;
    }


        private Boolean idIsExist(Long id){
            if(caseTreeMapper.findTreeById(id) instanceof CaseTreeNode){
                return true;
            }
            return false;
        }

        private CaseTreeNode jsonToNode(JsonObject jsonObject){
        Gson gson = new Gson();
        return gson.fromJson(jsonObject,CaseTreeNode.class);
        }

        private CaseTreeNode resultUpdate(Long id,CaseTreeNode currentNode){
            CaseTreeNode treeNode = caseTreeMapper.findTreeById(id);
            Field[] fields = treeNode.getClass().getDeclaredFields();
           /* for(Field field : fields){

            }*/
            return treeNode;//todo
        }

    @Cacheable(value = "tree")
    public String getTree() {
        String tree = null;
        return tree;
    }

    @CachePut(value = "tree")
    public String refreshTree() {
        String tree = null;
        return tree;
    }

}
