package com.testmanage.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "tm.dingding")
@Component
public class DingDingConf {

    private List<Config> configs;

    @Data
    public static class Config{
        private String name;
        private String token;
        private String content;
    }
}
