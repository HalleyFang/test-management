package com.testmanage.utils;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.UnsafeUtil;

import java.util.List;

/**
 * 这是一个线程安全的 json 转换器。
 */
public class JsonUtil {

    /**
     * 序列化时，jodd做了处理，是线程安全的
     */
    private static final JsonSerializer JSON_SERIALIZER = JsonSerializer.create().deep(true);
    /**
     * 创建线程本地变量储存 JsonParser，防止多线程并发问题
     */
    private static final ThreadLocal<JsonParser> JSON_PARSER_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> JsonParser.create());

    public static String serialize(Object source) {
        return JSON_SERIALIZER.serialize(source);
    }

    public static <T> T parse(String input, Class<T> targetType) {
        JsonParser jsonParser = JSON_PARSER_THREAD_LOCAL.get();
        char[] chars = UnsafeUtil.getChars(input);
        return jsonParser.parse(chars, targetType);
    }

    public static <T> List<T> parseList(String input, Class<?> targetType) {
        JsonParser jsonParser = JSON_PARSER_THREAD_LOCAL.get();
        return jsonParser.map(JsonParser.VALUES, targetType).parse(input);
    }
}
