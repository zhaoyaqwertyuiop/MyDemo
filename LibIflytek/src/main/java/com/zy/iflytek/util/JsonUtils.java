package com.zy.iflytek.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author ypgan
 * @Date 2022/11/18
 */
public class JsonUtils {
    private static final String TAG = "JsonUtils";

    private static final Gson gson = new Gson();

    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  <T> T toBean(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  <T> T toBean(JsonReader json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toBean(String json, @NonNull TypeToken<T> typeToken) {
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转array
     * @param json
     * @param typeToken type
     * @return
     */
    public static <T> List<T> toArray(String json, @NonNull TypeToken<List<T>> typeToken) {
        List<T> list = null;
        try {
            list = gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static <T> List<T> toList(String json, @NonNull Class<T> clazz) {
        TypeToken typeToken = TypeToken.getParameterized(List.class, clazz);
        return toArray(json, typeToken);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

}

