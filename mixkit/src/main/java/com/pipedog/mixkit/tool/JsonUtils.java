package com.pipedog.mixkit.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Json 解析工具，主要是用来获取处理不同结构的 Gson 对象
 * @author liang
 * @time 2022/01/12
 */
public class JsonUtils {

    // PUBLIC STATIC METHODS

    /**
     * 获取解析外层是 Map 的 Gson 对象
     */
    public static Gson getMapGson() {
        return new GsonBuilder().registerTypeAdapter(
                    new TypeToken<Map<String, Object>>(){}.getType(),
                    new NumberTypeAdapter())
                .create();
    }

    /**
     * 获取解析外层是 Array 的 Gson 对象
     */
    public static Gson getArrayGson() {
        return new GsonBuilder().registerTypeAdapter(
                new TypeToken<List<Object>>(){}.getType(),
                new NumberTypeAdapter())
                .create();
    }

}
