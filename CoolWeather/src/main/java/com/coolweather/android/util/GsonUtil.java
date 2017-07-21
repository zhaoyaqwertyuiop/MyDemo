package com.coolweather.android.util;

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
        gson = getGsonInstance();
    }

    public static Gson getGsonInstance() {
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    /** 将Json数据解析成相应的映射对象 */
    public static <T> T fromJson(String jsonData, Class<T> type) {
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 在创建完gson之后, 使用时注意自己注册的type类型 , 这时转换出的int 不会变成double
    public static final ArrayList<TreeMap<String, Object>> fromJsonListMap(String json) {
        ArrayList<TreeMap<String, Object>> mList = new ArrayList<TreeMap<String, Object>>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add((TreeMap<String, Object>) gson.fromJson(elem, new TypeToken<TreeMap<String, Object>>(){}.getType()));
        }
        return mList;
    }

    /** 将Json数据解析成相应的映射对象列表, 这个是正确写法,下面是错误写法 */
    public static final <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

    /** 这个是错误写法 */
    public static final <T> ArrayList<T> fromJsonList(String json) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Type type = new TypeToken<T>(){}.getType();
        T t = (T) new Object();
        ParameterizedType parameterizedType = (ParameterizedType) t.getClass().getGenericSuperclass();

        Class<T> entityClass = (Class<T>)(parameterizedType.getActualTypeArguments()[0]);
        for(final JsonElement elem : array){
//            mList.add((T) gson.fromJson(elem, type));
            mList.add(gson.fromJson(elem, entityClass));
        }
        return mList;
    }
}
