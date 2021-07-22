package com.testmanage.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.mapper.CaseTreeMapper;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
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
            currentNode.setIs_v(UserContext.get().getIsV());
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
        JsonObject preJson = treeJson.get("preNode").getAsJsonObject();
        JsonObject postJson = treeJson.get("postNode").getAsJsonObject();
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
        List<CaseTreeNode> parentTree = caseTreeMapper.findTreeByParent(parentId,UserContext.get().getIsV());
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
        List<CaseTreeNode> treeNodeList = caseTreeMapper.findTreeByParent(id,UserContext.get().getIsV());
        if (treeNodeList.size() == 0) {
            return null;
        }
        for (CaseTreeNode node : treeNodeList) {
            if (node.getIs_dir() && !node.getIs_delete()) {
                if (id == 0) {
                    //根节点
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("label", node.getLabel());
                    jsonObject.addProperty("is_dir", true);
                    jsonObject.addProperty("icon", "el-icon-folder");
                    jsonObject.addProperty("status", 0);
                    JsonArray j = new JsonArray();
                    jsonObject.add("children", j);
                    map = new HashMap<>();
                    map.put(node.getId(), j);
                } else {
                    JsonArray jsonArray = map.get(id).getAsJsonArray();
                    JsonObject j2 = new JsonObject();
                    j2.addProperty("label", node.getLabel());
                    j2.addProperty("is_dir", true);
                    j2.addProperty("icon", "el-icon-folder");
                    j2.addProperty("status", 0);
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
                JsonObject nodeJson = JsonParse.StringToJson(str);
                nodeJson.addProperty("icon", "el-icon-tickets");
                jsonArray.add(nodeJson);
            }
            treeJson.add(jsonObject);
        }

        return treeJson.toString();
    }


    public void analysisRequest(String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject parentNode = bodyJson.getAsJsonObject("parentNode");
        Long parentId = tmpNode(parentNode);
        JsonObject preNode = bodyJson.getAsJsonObject("preNode");
        Long preId = tmpNode(preNode);
        JsonObject postNode = bodyJson.getAsJsonObject("postNode");
        Long postId = tmpNode(postNode);
    }


    private Long tmpNode(JsonObject jsonObject) throws Exception {
        if(jsonObject == null){
            return null;
        }
        String label = jsonObject.get("label").getAsString();
        if(label==null){
            return 0L;
        }
        Boolean isDir = jsonObject.get("is_dir").getAsBoolean();
        String case_id = jsonObject.get("case_id").getAsString();
        Long id=null;
        if(isDir) {
            id = caseTreeMapper.findNodeByName(label, UserContext.get().getIsV());
        }else {
            id = caseTreeMapper.findNodeByCaseId(case_id);
        }
        if(id == null){
            throw new Exception("树结构非法");
        }
        return id;
    }

/*    private CaseTreeNode currentNode(JsonObject jsonObject){

    }*/

}
