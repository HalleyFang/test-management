package com.testmanage.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.mapper.CaseTreeMapper;
import com.testmanage.utils.JsonParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaseTreeService {

    @Autowired
    CaseTreeMapper caseTreeMapper;

    @CacheEvict(value = "tree")
    public synchronized void addTree(JsonObject treeJson) throws Exception {
        Map<String, Object> resultMap = analysisTreeJson(treeJson);
        if (!idIsExist((Long) resultMap.get("id"))) {
            updateTree(treeJson);
        } else {
            CaseTreeNode currentNode = (CaseTreeNode) resultMap.get("currentNode");
            if (!(Boolean) resultMap.get("isOnly")) {
                if (idIsExist((Long) resultMap.get("preId"))) {
                    currentNode.setPre_id((Long) resultMap.get("preId"));
                }
                if (idIsExist((Long) resultMap.get("postId"))) {
                    currentNode.setPost_id((Long) resultMap.get("postId"));
                }
            }
            caseTreeMapper.addTree(currentNode);
        }

    }

    @CacheEvict(value = "tree")
    public synchronized void updateTree(JsonObject treeJson) throws Exception {
        Map<String, Object> resultMap = analysisTreeJson(treeJson);
        CaseTreeNode currentNode = (CaseTreeNode) resultMap.get("currentNode");
        if (!idIsExist((Long) resultMap.get("id"))) {
            addTree(treeJson);
        } else {
            if (!((boolean) resultMap.get("isOnly"))) {
                CaseTreeNode treeNode = caseTreeMapper.findTreeById((Long) resultMap.get("id"));
                if (treeNode.getPre_id() != resultMap.get("preId")
                        && treeNode.getPost_id() != resultMap.get("postId")) {
                    CaseTreeNode oldPreNode = caseTreeMapper.findTreeById(treeNode.getPre_id());
                    CaseTreeNode oldPostNode = caseTreeMapper.findTreeById(treeNode.getPost_id());
                    oldPreNode.setPost_id(treeNode.getPost_id());
                    oldPostNode.setPre_id(treeNode.getPre_id());
                    caseTreeMapper.updateTree(oldPreNode);
                    caseTreeMapper.updateTree(oldPostNode);
                    currentNode.setPre_id((Long) resultMap.get("preId"));
                    currentNode.setPost_id((Long) resultMap.get("postId"));
                }
            }
            caseTreeMapper.updateTree(currentNode);            //todo 没变的字段不更新
        }
    }

    private Map<String, Object> analysisTreeJson(JsonObject treeJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        JsonObject currentJson = treeJson.get("currentNode").getAsJsonObject();
        if (currentJson == null) {
            throw new Exception("node is not invalid");
        }
        JsonObject preJson = treeJson.get("currentNode").getAsJsonObject();
        JsonObject postJson = treeJson.get("currentNode").getAsJsonObject();
        Long preId = -1L;
        Long postId = -1L;
        Long id = currentJson.get("id").getAsLong();
        if (preJson != null) {
            preId = preJson.get("id").getAsLong();
            if (!idIsExist(preId)) {
                throw new Exception("pre node is not invalid");
            }
        }
        if (postJson != null) {
            postId = postJson.get("id").getAsLong();
            if (!idIsExist(postId)) {
                throw new Exception("post node is not invalid");
            }
        }
        Long parentId = currentJson.get("parentId").getAsLong();
        List<CaseTreeNode> parentTree = caseTreeMapper.findTreeByParent(parentId);
        Boolean isOnly = false;
        if (parentTree.size() <= 1 && parentTree.get(0).getId().equals(id)) {
            isOnly = true;
        }
        if (!idIsExist(preId) && !idIsExist(postId) && !isOnly) {
            throw new Exception("pre and post node are not invalid");
        }

        resultMap.put("id", id);
        resultMap.put("currentNode", jsonToNode(currentJson));
        resultMap.put("preId", preId);
        resultMap.put("preNode", jsonToNode(preJson));
        resultMap.put("postId", postId);
        resultMap.put("postNode", jsonToNode(postJson));
        resultMap.put("isOnly", isOnly);
        return resultMap;
    }


    private Boolean idIsExist(Long id) {
        if (caseTreeMapper.findTreeById(id) instanceof CaseTreeNode) {
            return true;
        }
        return false;
    }

    private CaseTreeNode jsonToNode(JsonObject jsonObject) {
        Gson gson = new Gson();//todo
        return gson.fromJson(jsonObject, CaseTreeNode.class);
    }

    private CaseTreeNode resultUpdate(Long id, CaseTreeNode currentNode) {
        CaseTreeNode treeNode = caseTreeMapper.findTreeById(id);
        Field[] fields = treeNode.getClass().getDeclaredFields();
           /* for(Field field : fields){

            }*/
        return treeNode;//todo
    }

    @Cacheable(value = "tree")
    public String getTree() {
        return generateTree(0L, null, null);
    }

    @CachePut(value = "tree")
    public String refreshTree() {
        return generateTree(0L, null, null);
    }


    private String generateTree(Long id, JsonObject jsonObject, Map<Long, JsonArray> map) {
        JsonArray treeJson = new JsonArray();
        List<CaseTreeNode> treeNodeList = caseTreeMapper.findTreeByParent(id);
        if (treeNodeList.size() == 0) {
            return null;
        }
        for (CaseTreeNode node : treeNodeList) {
            if (node.getIs_dir() && !node.getIs_delete()) {
                if (id == 0) {
                    //根节点
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("label", node.getLabel());
                    JsonArray j = new JsonArray();
                    jsonObject.add("children", j);
                    map = new HashMap<>();
                    map.put(node.getId(), j);
                } else {
                    JsonArray jsonArray = map.get(id).getAsJsonArray();
                    JsonObject j2 = new JsonObject();
                    j2.addProperty("label", node.getLabel());
                    JsonArray j = new JsonArray();
                    j2.add("children", j);
                    jsonArray.add(j2);
                    map.put(node.getId(), j);
                }
                generateTree(node.getId(), jsonObject, map);
            }
            if (!node.getIs_dir() && !node.getIs_delete()) {
                JsonArray jsonArray = map.get(id).getAsJsonArray();
                String str = JsonParse.getGson().toJson(node);
                jsonArray.add(JsonParse.StringToJson(str));
            }
            treeJson.add(jsonObject);
        }

        return treeJson.toString();
    }

}
