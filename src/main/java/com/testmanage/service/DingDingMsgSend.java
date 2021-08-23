package com.testmanage.service;

import com.testmanage.entity.DingDingConf;
import com.testmanage.utils.DingDingMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DingDingMsgSend {

    @Autowired
    DingDingConf dingDingConf;

    public synchronized void sendDingDingMsg(String name) throws Exception {
        DingDingConf.Config config = getConfig(name);
        if(config == null){
            throw new Exception("钉钉消息配置不正确");
        }
        String dingDingToken=config.getToken();
        Map<String,Object> json=new HashMap();
        Map<String,Object> text=new HashMap();
        json.put("msgtype","text");
        text.put("content",config.getContent());
        json.put("text",text);
        DingDingMsg.sendPostByMap(dingDingToken, json);
    }


    private DingDingConf.Config getConfig(String name){
        List<DingDingConf.Config> configs = dingDingConf.getConfigs();
        for(DingDingConf.Config config:configs){
            if(config.getName().equalsIgnoreCase(name)){
                return config;
            }
        }
        return null;
    }
}
