package com.zylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.zylibrary.ZYLibrary;

import java.util.List;

/**
 * Created by zhaoya on 2016/9/13.
 */
public class SharedPreferenceUtil {
    public static final String DATA_KEY = "data";

    private static SharedPreferenceUtil instence;
    private static SharedPreferences sp;

    public static SharedPreferenceUtil getInstence() {
        if (instence == null) {
            instence = new SharedPreferenceUtil();
        }
        sp = ZYLibrary.app().getSharedPreferences(DATA_KEY, Context.MODE_PRIVATE);
        return instence;
    }

    public static SharedPreferenceUtil getInstence(String spName) {
        if (instence == null) {
            instence = new SharedPreferenceUtil();
        }
        sp = ZYLibrary.app().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return instence;
    }

    /** 存储基本类型 */
    public void saveValue(String key, Object value) {
        if (value == null) {
            sp.edit().remove(key).apply();
        } else if (value instanceof String) {
            sp.edit().putString(key, (String)value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Float) {
            sp.edit().putFloat(key, (Float) value).commit();
        } else if (value instanceof Long) {
            sp.edit().putLong(key, (Long) value).commit();
        } else {
            String string = GsonUtil.gson().toJson(value);
            sp.edit().putString(key, string).apply();
        }
    }

    /** 存储对象 */
    public void saveObject(String key, Object value) {
        if (value == null) {
            sp.edit().remove(key).apply();
        } else {
            String string = GsonUtil.gson().toJson(value);
            sp.edit().putString(key, string).apply();
        }
    }

    /**
     * 读取基本类型
     */
    public <T> T getValue(String key, Object defValue) {
        T t = null;
        if (defValue instanceof String) {
            t = (T) sp.getString(key, (String)defValue);
        } else if (defValue instanceof Integer) {
            Integer value = sp.getInt(key, (Integer) defValue);
            t = (T) value;
        } else if (defValue instanceof Boolean) {
            Boolean value = sp.getBoolean(key, (Boolean) defValue);
            t = (T) value;
        } else if (defValue instanceof Float) {
            Float value = sp.getFloat(key, (Float) defValue);
            t = (T) value;
        } else if (defValue instanceof Long) {
            Long value = sp.getLong(key, (Long) defValue);
            t = (T) value;
        }
        return t;
    }

    /** 取出对象 */
    public <T> T getObject(String key, Class<T> type) {
        String string = sp.getString(key, null);
        if (string == null) {
            return null;
        } else {
            return GsonUtil.gson().fromJson(string, type);
        }
    }

    /** 保存list数据 */
    public void saveList(String key, List<?> value) {
        saveObject(key, value);
    }

    /** 取出list数据 */
    public <T> List<T> getList(String key, TypeToken<List<T>> typeToken) {
        String string = sp.getString(key, null);
        if (string == null) {
            return null;
        } else {
            return GsonUtil.gson().fromJson(string, typeToken.getType());
        }
    }
}
