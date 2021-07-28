package com.testmanage.service;

import com.google.gson.*;
import com.testmanage.entity.CaseTreeNode;
import com.testmanage.entity.TaskCase;
import com.testmanage.mapper.CaseTreeMapper;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import com.testmanage.utils.SequenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

@Service
public class CaseTreeService {

    @Autowired
    CaseTreeMapper caseTreeMapper;

    @Autowired
    SequenceUtil sequenceUtil;

    @Autowired
    TaskCaseService taskCaseService;

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
    public synchronized void addTree(Map<String, Object> map) throws Exception {
        CaseTreeNode node = (CaseTreeNode) map.get("currentNode");
        Long id = node.getId();
        if (id == null) {
            id = Long.valueOf(sequenceUtil.getNext("treeId"));
            node.setId(id);
        }
        if (idIsExist(id)) {
            caseTreeMapper.updateTree(node);
        } else {
            String label = node.getLabel();
            Long labelId = caseTreeMapper.findNodeByName(label, UserContext.get().getIsV());
            if (labelId instanceof Long) {
                throw new Exception("名称不能重复");
            }
            Long parentId = (Long) map.get("parentId");
            if (parentId == null) {
                parentId = 0L;
            }
            node.setParent_id(parentId);
            node.setIs_v(UserContext.get().getIsV());
            caseTreeMapper.addTree(node);
            Long preId = (Long) map.get("preId");
            if (preId != null) {
                node.setPre_id(preId);
                CaseTreeNode preNode = new CaseTreeNode();
                preNode.setId(preId);
                preNode.setPost_id(id);
                preNode.setUpdate_user(UserContext.get().getUsername());
                caseTreeMapper.updateTree(preNode);
            }
            Long postId = (Long) map.get("postId");
            if (postId != null) {
                node.setPost_id(postId);
                CaseTreeNode postNode = new CaseTreeNode();
                postNode.setId(postId);
                postNode.setPre_id(id);
                postNode.setUpdate_user(UserContext.get().getUsername());
                caseTreeMapper.updateTree(postNode);
            }

        }

    }

    @CacheEvict(value = "tree")
    public synchronized void deleteTree(Long id) {
        CaseTreeNode currentNode = caseTreeMapper.findTreeById(id);
        if (currentNode != null) {
            Long preId = currentNode.getPre_id();
            Long postId = currentNode.getPost_id();
            if (preId != null) {
                CaseTreeNode caseTreeNode = new CaseTreeNode();
                caseTreeNode.setId(preId);
                caseTreeNode.setPost_id(postId);
                caseTreeMapper.updateTree(caseTreeNode);
            }
            if (postId != null) {
                CaseTreeNode caseTreeNode = new CaseTreeNode();
                caseTreeNode.setId(postId);
                caseTreeNode.setPre_id(preId);
                caseTreeMapper.updateTree(caseTreeNode);
            }
            caseTreeMapper.deleteTreeById(id);
        }
    }

    @CacheEvict(value = "tree")
    public synchronized void deleteTree(String caseId) {
        Long id = caseTreeMapper.findNodeByCaseId(caseId);
        deleteTree(id);
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
        List<CaseTreeNode> parentTree = caseTreeMapper.findTreeByParent(parentId, UserContext.get().getIsV());
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


    //todo 排序
    private String generateTree(Long parentId, JsonObject jsonObject, Map<Long, JsonArray> map) {
        JsonArray treeJson = new JsonArray();//用来存放树结果，递归多次创建并不影响最终的结果
        List<CaseTreeNode> treeNodeList = caseTreeMapper.findTreeByParent(parentId, UserContext.get().getIsV());
        if (treeNodeList.size() == 0) {
            return null;
        }
        for (CaseTreeNode node : treeNodeList) {
            if (node.getIs_dir() && !node.getIs_delete()) {
                if (parentId == 0) {
                    //根节点
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("id", node.getId());
                    jsonObject.addProperty("label", node.getLabel());
                    jsonObject.addProperty("is_dir", true);
                    jsonObject.addProperty("icon", "el-icon-folder");
                    jsonObject.addProperty("status", 0);
                    JsonArray j = new JsonArray();
                    jsonObject.add("children", j);
                    map = new HashMap<>();
                    map.put(node.getId(), j);//将children和parentId关心存入map
                } else {
                    //中间节点
                    JsonArray jsonArray = map.get(parentId).getAsJsonArray();
                    JsonObject j2 = new JsonObject();
                    j2.addProperty("id", node.getId());
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
                JsonArray jsonArray = map.get(parentId).getAsJsonArray();//对children对象进行修改
                String str = JsonParse.getGson().toJson(node);
                JsonObject nodeJson = JsonParse.StringToJson(str);
                nodeJson.addProperty("icon", "el-icon-tickets");
                jsonArray.add(nodeJson);
            }
            treeJson.add(jsonObject);//只关心最终结果，将jsonObject加入结果
        }

        return treeJson.toString();
    }


    public String getTaskTree(Long taskId,List<Long> treeId) throws Exception {
        Stack stack = taskTree(treeId,null);
        return taskTreeGenerate(taskId,stack);
    }

    private Stack taskTree(List<Long> treeId, Stack<Map<Long,List<Long>>> treeStack) throws Exception {
        if(treeId.size()==0){
            throw new Exception("请关联用例");
        }
        Map<Long,List<Long>> map = new HashMap<>();
        for(Long tId : treeId){
            Long parentId = 0L;
            if(tId!=0L){
            CaseTreeNode treeNode = caseTreeMapper.findTreeById(tId);
            parentId = treeNode.getParent_id();
            }
            if(!map.containsKey(parentId)){
                map.put(parentId,new ArrayList<>());
            }
            List<Long> list = map.get(parentId);
            list.add(tId);
            map.put(parentId,list);
        }
        if(treeStack==null){
            treeStack = new Stack<>();
        }
        treeStack.push(map);
        if(map.size()>1 || !map.containsKey(0L)){
            List<Long> parenIdList = new ArrayList<>();
            for(Map.Entry entry: map.entrySet()){
                parenIdList.add((Long) entry.getKey());
            }
            taskTree(parenIdList,treeStack);
        }
        return treeStack;
    }

    private String taskTreeGenerate(Long taskId,Stack<Map<Long,List<Long>>> treeStack){
        if(treeStack.empty()){
            return null;
        }
        Map<Long, JsonArray> parentMap = new HashMap<>();
        List<JsonObject> jsonObjectList = new ArrayList<>();
        Integer stackSize = treeStack.size();
        for (int i=0;i<stackSize;i++){
            Map<Long,List<Long>> treeMap = treeStack.pop();
            //遍历节点map
            for(Map.Entry entry:treeMap.entrySet()){
                List<Long> listNode = (List<Long>) entry.getValue();
                    for(Long id:listNode){
                        if(id!=0L){
                            CaseTreeNode caseTreeNode = caseTreeMapper.findTreeById(id);
                            JsonObject jsonObject = new JsonObject();
                            if(caseTreeNode.getIs_dir()){
                                if(caseTreeNode.getParent_id()==0L){
                                    caseTreeNode.setParent_id(null);
                                }
                                jsonObject = JsonParse.StringToJson(JsonParse.getGson().toJson(caseTreeNode));
                                jsonObject.addProperty("icon", "el-icon-folder");
                                JsonArray j = new JsonArray();
                                jsonObject.add("children", j);
                                parentMap.put(id, j);
                            }else {
                                JsonArray jsonArray = parentMap.get(entry.getKey());
                                TaskCase taskCase = taskCaseService.query(taskId, caseTreeNode.getCase_id());
                                jsonObject = JsonParse.StringToJson(JsonParse.getGson().toJson(caseTreeNode));
                                jsonObject.addProperty("result", taskCase.getCase_status());
                                jsonObject.addProperty("icon", "el-icon-tickets");
                                jsonArray.add(jsonObject);
                            }
                            if((Long)entry.getKey()==0L) {
                                jsonObjectList.add(jsonObject);
                            }
                        }
                    }
            }
        }
        JsonArray treeJson = new JsonArray();
        for(JsonObject jo:jsonObjectList){
            treeJson.add(jo);
        }
        return treeJson.toString();
    }

    public Map<String, Object> analysisRequest(String body, String type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        JsonObject bodyJson = JsonParse.StringToJson(body);
        JsonObject currentNode = bodyJson.getAsJsonObject("data");
        if (type.equalsIgnoreCase("tree")) {
            currentNode.addProperty("is_dir", true);
            currentNode.addProperty("status", 0);
        } else if (type.equalsIgnoreCase("case")) {
            currentNode.addProperty("is_dir", false);
            currentNode.addProperty("status", -1);
        }
        CaseTreeNode caseTreeNode = JsonParse.getGson().fromJson(currentNode, CaseTreeNode.class);
        map.put("currentNode", caseTreeNode);
        JsonObject parentNode = bodyJson.getAsJsonObject("parentNode");
        Long parentId = tmpNode(parentNode);
        map.put("parentId", parentId);
        JsonObject preNode = bodyJson.get("preNode") instanceof JsonNull ? null : bodyJson.getAsJsonObject("preNode");
        Long preId = tmpNode(preNode);
        map.put("preId", preId);
        JsonObject postNode = bodyJson.get("postNode") instanceof JsonNull ? null : bodyJson.getAsJsonObject("postNode");
        Long postId = tmpNode(postNode);
        map.put("postId", postId);
        return map;
    }


    private Long tmpNode(JsonObject jsonObject) throws Exception {
        if (jsonObject == null) {
            return null;
        }
        String label = jsonObject.get("label") instanceof JsonNull || jsonObject.get("label") == null
                ? null : jsonObject.get("label").getAsString();
        if (label == null) {
            return 0L;
        }
        Boolean isDir = jsonObject.get("is_dir") instanceof JsonNull || jsonObject.get("is_dir") == null
                ? true : jsonObject.get("is_dir").getAsBoolean();
        String case_id = jsonObject.get("case_id") instanceof JsonNull || jsonObject.get("case_id") == null
                ? null : jsonObject.get("case_id").getAsString();
        Long id = null;
        if (isDir) {
            id = caseTreeMapper.findNodeByName(label, UserContext.get().getIsV());
        } else {
            id = caseTreeMapper.findNodeByCaseId(case_id);
        }
        if (id == null) {
            throw new Exception("树结构非法");
        }
        return id;
    }

    public CaseTreeNode getTreeById(Long id){
        return caseTreeMapper.findTreeById(id);
    }

}
