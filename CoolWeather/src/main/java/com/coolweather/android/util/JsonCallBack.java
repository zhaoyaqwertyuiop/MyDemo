package com.coolweather.android.util;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoya on 2016/11/24.
 */
public class JsonCallBack implements Callback.CommonCallback<String> {

    private static final String TAG = JsonCallBack.class.getSimpleName();
    protected final static String SUCCESS = "success".intern();
    protected final static String RESULT = "result".intern();
    protected final static String MSG = "msg".intern();
    protected final static String MESSAGE = "message".intern();
    protected final static String ERROR_CODE = "errorCode".intern();

    protected OnSuccess onSuccess; // 成功
    protected OnFailure onFailure; // 服务器返回false
    protected OnError onError; // 连接服务器失败
    protected OnCancelled onCancelled; // 取消
    protected OnFinish onFinish;

    public JsonCallBack() {

        onFailure = new OnFailure() {
            @Override
            public boolean onFailure(String msg, String errcode) {
                Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        onError = new OnError() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Toast.makeText(x.app(), "错误代码：" + responseCode + "，" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    // ...
                } else { // 其他错误
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };

        onCancelled = new OnCancelled() {
            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }
        };
    }

    public JsonCallBack on(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public JsonCallBack on(OnError onError) {
        this.onError = onError;
        return this;
    }

    public JsonCallBack on(OnCancelled onCancelled) {
        this.onCancelled = onCancelled;
        return this;
    }

    public JsonCallBack on(OnFinish onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    @Override
    public void onSuccess(String result) {
        Log.d(TAG, "result=" + result);
        try {
            JsonReader reader = new JsonReader(new StringReader(result.toString()));
            JsonResult jsonResult = readJson(reader);
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
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        if (onError != null) {
            onError.onError(ex, isOnCallback);
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        if (onCancelled != null) {
            onCancelled.onCancelled(cex);
        }
    }

    @Override
    public void onFinished() {
        if (onFinish != null) {
            onFinish.onFinish();
        }
    }

    public interface OnSuccess {
        void onSuccess(JsonResult result);
    }

    public interface OnFailure {
        boolean onFailure(String msg, String errcode);
    }

    public interface OnError {
        void onError(Throwable ex, boolean isOnCallback);
    }

    public interface OnCancelled {
        void onCancelled(CancelledException cex);
    }

    public interface OnFinish {
        void onFinish();
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

    public static class JsonResult {
        public boolean success = true;
        public String msg;
        public String errorCode;
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
//        data = readData(tagname, reader, onFinish, data);
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
            return GsonUtil.getGsonInstance().fromJson(reader, type);
        }
    }

    private static final String THIS$0 = "this$0".intern();
}
