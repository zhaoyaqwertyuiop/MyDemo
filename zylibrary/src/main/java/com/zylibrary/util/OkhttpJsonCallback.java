package com.zylibrary.util;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhaoya on 2017/4/10.
 */

public class OkhttpJsonCallback extends Callback<String> {

    private final static String TAG = OkhttpJsonCallback.class.getSimpleName();
    protected OnSuccess onSuccess; // 成功
    protected OnFailure onFailure; // 服务器返回false
    protected OnError onError; // 服务器错误
    protected OnFinish onFinish;

    protected final static String SUCCESS = "success".intern();
    protected final static String RESULT = "result".intern();
    protected final static String MSG = "msg".intern();
    protected final static String MESSAGE = "message".intern();
    protected final static String ERROR_CODE = "errorCode".intern();

    public OkhttpJsonCallback() {
        onFailure = new OnFailure() {
            @Override
            public void onFailure(String msg, String errcode) {
                ToastUtil.showLong(errcode + " : " + msg);
            }
        };

        onError = new OnError() {
            @Override
            public void onError(Exception e) {
                if (e instanceof SocketTimeoutException) {
                    ToastUtil.showLong("连接超时");
                } else {
                    ToastUtil.showLong(e.toString());
                }
            }
        };
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (onError != null) {
            onError.onError(e);
        }
        if (onFinish != null) {
            onFinish.onFinish();
        }
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            LogUtil.d(TAG, "response=" + response);
            JsonReader reader = new JsonReader(new StringReader(response));
            JsonResult jsonResult = readJson(reader);
            jsonResult.result = response;
            if (jsonResult.success) {
                if (onSuccess != null) {
                    onSuccess.onSuccess(jsonResult);
                }
            } else {
                if (onFailure != null) {
                    onFailure.onFailure(jsonResult.msg, jsonResult.errorCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (onFinish != null) {
            onFinish.onFinish();
        }
    }

    protected JsonResult readJson(JsonReader reader) throws IOException, IllegalAccessException {
        JsonResult result = new JsonResult();
        reader.beginObject();
        while(reader.hasNext()) {
            String tagname = reader.nextName().intern();
            if (SUCCESS == tagname || RESULT == tagname) {
                if (reader.peek() == JsonToken.BOOLEAN) {
                    result.success = reader.nextBoolean();
                    readData(tagname, reader, result.success);
                } else {
                    reader.skipValue();
                }
            } else if (MSG == tagname || MESSAGE == tagname) {
                if (reader.peek() == JsonToken.STRING) {
                    result.msg = reader.nextString();
                    readData(tagname, reader, result.msg);
                } else {
                    reader.skipValue();
                }
            } else if (ERROR_CODE == tagname) {
                if (reader.peek() == JsonToken.STRING) {
                    result.errorCode = reader.nextString();
                    readData(tagname, reader, result.errorCode);
                } else {
                    reader.skipValue();
                }
            } else {
                Object data = readData(tagname, reader);
                if (data == null) {
                    reader.skipValue();
                }
            }
        }
        reader.endObject();
        return result;
    }

    public OkhttpJsonCallback on(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public OkhttpJsonCallback on(OnFailure onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public OkhttpJsonCallback on(OnError onError) {
        this.onError = onError;
        return this;
    }

    public OkhttpJsonCallback on(OnFinish onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public interface OnSuccess {
        void onSuccess(JsonResult result);
    }

    public interface OnFailure {
        void onFailure(String msg, String errcode);
    }

    public interface OnError {
        void onError(Exception e);
    }

    public interface OnFinish {
        void onFinish();
    }

    public static class JsonResult {
        public boolean success = true;
        public String msg;
        public String errorCode;
        public String result;
    }

    private Field findField(String tagname, Object obj) {
        if (obj == null) return null;
        String internName = tagname.intern();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().intern() == internName) {
                return field;
            }
        }
        return null;
    }

    private Object readData(String tagname, JsonReader reader, Object obj, Object data) throws IOException, IllegalAccessException {
        Field field = findField(tagname, obj);
        if (field == null) return data;
        if (data == null) {
            Type type = field.getGenericType();
            data = readObject(reader, type, obj);
        }
        if ( ! field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(obj, data);
        return data;
    }

    private Object readData(String tagname, JsonReader reader) throws IOException, IllegalAccessException {
        return readData(tagname, reader, null);
    }

    private Object readData(String tagname, JsonReader reader, Object data) throws IOException, IllegalAccessException {
        data = readData(tagname, reader, onSuccess, data);
        data = readData(tagname, reader, onFailure, data);
        return data;
    }

    @SuppressWarnings("rawtypes")
    private Type findActualTypeInParameterizedType(TypeVariable typeVar, Type... types) {
        for (Type type : types) {
            if (type == null) continue;
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType)type;
                Type rType = pType.getRawType();
                if (rType instanceof Class) {
                    Class clazz = (Class)rType;
                    Type[] aTypes = pType.getActualTypeArguments();
                    TypeVariable[] tvs = clazz.getTypeParameters();
                    for (int i = 0; i < tvs.length; i++) {
                        if (tvs[i].equals(typeVar)) {
                            return aTypes[i];
                        }
                    }
                    if (clazz != Object.class) {
                        // 继续查找父类
                        Type aType = findActualTypeInParameterizedType(typeVar, clazz.getGenericSuperclass());
                        if (aType != null) return aType;
                        aType = findActualTypeInParameterizedType(typeVar, clazz.getGenericInterfaces());
                        if (aType != null) return aType;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private Type findActualType(TypeVariable typeVar, Object obj) {
        try {
            Class clazz = obj.getClass();
            //查找父类中的类型变量
            Type aType = findActualTypeInParameterizedType(typeVar, clazz.getGenericSuperclass());
            if (aType != null) return aType;
            //查找接口中的类型变量
            aType = findActualTypeInParameterizedType(typeVar, clazz.getGenericInterfaces());
            if (aType != null) return aType;
            // 查找外部类对象
            Field[] fields = clazz.getDeclaredFields();
            // 查找外部对象
            for (Field f : fields) {
                if (f.getName().intern() == THIS$0) {
                    f.setAccessible(true);
                    Object encloseObj = f.get(obj);
                    if (encloseObj != null) {
                        // 对外部类对象进行查找
                        return findActualType(typeVar, encloseObj);
                    }
                    break;
                }
            }
            return null;
        } catch (Throwable tr) {
            tr.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    private Type findListType(Type type, Object obj) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)type;
            if (pType.getRawType() == List.class) {
                Type eType = pType.getActualTypeArguments()[0];
                while (eType instanceof TypeVariable) {
                    eType = findActualType((TypeVariable)eType, obj);
                }
                return eType;
            }
        }
        return null;
    }

    private Object readObject(JsonReader reader, Type type, Object obj) throws IOException {
        Type listType = findListType(type, obj);
        if (listType != null) {
            List<Object> list = new ArrayList<Object>();
            reader.beginArray();
            while(reader.hasNext()) {
                list.add(readObject(reader, listType, obj));
            }
            reader.endArray();
            return list;
        } else {
            return new GsonBuilder().create().fromJson(reader, type);
        }
    }

    private static final String THIS$0 = "this$0".intern();
}
