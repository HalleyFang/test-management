package com.testmanage.utils;

import com.testmanage.mapper.CaseIdSequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SequenceUtil {

    @Autowired
    CaseIdSequenceMapper caseIdSequenceMapper;

    private static final SequenceUtil sequenceUtil = new SequenceUtil();

    private SequenceUtil() {

    }

    ;

    public static SequenceUtil getSequenceUtil() {
        return sequenceUtil;
    }

    public synchronized Integer getNext() {
        Integer id = caseIdSequenceMapper.getNext();
        if (id <= 0) {
            id = 1;
        }
        caseIdSequenceMapper.update(id + 1);
        return id;
    }
}
