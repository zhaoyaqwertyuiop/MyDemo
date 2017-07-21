package com.zylibrary.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/10.
 * 封装的GSON解析工具类，提供泛型参数
 */
public class GsonUtil {

    private static Gson gson;
    public static final GsonBuilder gsonBuilder = new GsonBuilder();

    private GsonUtil() {
        gson = gson();
    }

    public static Gson gson() {
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    /** 将Json数据解析成相应的映射对象 */
    public static <T> T fromJson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    /** 将Json数据解析成相应的映射对象列表 */
    public static final <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

    // 解析成list<Map>, 使用时注意自己注册的type类型,这时转换出的int,long不会变成double
    public static final ArrayList<TreeMap<String, Object>> fromJsonListMap(String json) {
        ArrayList<TreeMap<String, Object>> mList = new ArrayList<TreeMap<String, Object>>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add((TreeMap<String, Object>) gson.fromJson(elem, new TypeToken<TreeMap<String, Object>>(){}.getType()));
        }
        return mList;
    }
}
