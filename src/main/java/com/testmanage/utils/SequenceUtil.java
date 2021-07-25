package com.testmanage.utils;

import com.testmanage.mapper.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SequenceUtil {

    @Autowired
    SequenceMapper caseIdSequenceMapper;


    public synchronized Integer getNext(String name) {
        Integer id = caseIdSequenceMapper.getNext(name);
        if (id <= 0) {
            id = 1;
        }
        caseIdSequenceMapper.update(name, id + 1);
        return id;
    }
}
