package com.testmanage.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonParse {

    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    public static String JsonToString(JsonObject jsonObject) {
        return gson.toJson(jsonObject);
    }

    public static JsonObject StringToJson(String str) {
        return gson.fromJson(str, JsonObject.class);
    }
}
