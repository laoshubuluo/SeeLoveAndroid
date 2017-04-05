package com.tianyu.seelove.utils;

import com.google.gson.Gson;

/**
 * author : L.jinzhu
 * date : 2015/8/1
 * introduce : json解析工具
 */
public class GsonUtil {
    private static Gson gson = new Gson();

    private GsonUtil() {
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
