package com.zy.iflytek.voice;

import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.zy.iflytek.util.JsonUtils;
import com.zy.iflytek.voice.bean.Feature;
import com.zy.iflytek.voice.req.CreatFeatureReq;
import com.zy.iflytek.voice.req.CreatGroupReq;
import com.zy.iflytek.voice.req.DeleteFeatureReq;
import com.zy.iflytek.voice.req.SearchFeatureReq;
import com.zy.iflytek.voice.res.CreatFeatureRes;
import com.zy.iflytek.voice.res.CreatGroupRes;
import com.zy.iflytek.voice.res.DeletaFeatureRes;
import com.zy.iflytek.voice.res.QueryFeatureListRes;
import com.zy.iflytek.voice.res.SearchFeatureRes;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author zhaoya
 * @Date 2024/12/27 17:32
 * @describe 声纹识别
 */
public enum VoiceRecogManager {
    o;
    private final String url = "https://api.xf-yun.com/v1/private/s782b4996";
//    private final String APPID = "21fc7db0";
//    private final String apiSecret = "ZDdkYTlhY2YwNDZiM2NkZjA1NTM4NTA3";
//    private final String apiKey = "c853fcb0efe3770688cfe67cc89f74b1";

    private final String APPID = "c08ef16b";
    private final String apiSecret = "ZGJlNjI4MTEwNzU5MDMwZTFkZmRiNjY2";
    private final String apiKey = "fbe0b21407ee8642d05bba116da0b51a";

    private final OkHttpClient client = new OkHttpClient();

    private String groupId = "voice_groupId";

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    //构建url
    public static String assembleRequestUrl(String requestUrl, String apiKey, String apiSecret) {
        URL url = null;
        // 替换调schema前缀 ，原因是URL库不支持解析包含ws,wss schema的url
        String  httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://","https://" );
        try {
            url = new URL(httpRequestUrl);
            //获取当前日期并格式化
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());

            String host = url.getHost();
            if (url.getPort()!=80 && url.getPort() !=443){
                host = host +":"+String.valueOf(url.getPort());
            }
            StringBuilder builder = new StringBuilder("host: ").append(host).append("\n").//
                    append("date: ").append(date).append("\n").//
                    append("POST ").append(url.getPath()).append(" HTTP/1.1");
            Charset charset = Charset.forName("UTF-8");
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
//            String sha = Base64.getEncoder().encodeToString(hexDigits);
            String sha =  EncodeUtils.base64Encode2String(hexDigits);

            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
//            String authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
            String authBase = EncodeUtils.base64Encode2String(authorization.getBytes(charset));
            return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));
        } catch (Exception e) {
            throw new RuntimeException("assemble requestUrl error:"+e.getMessage());
        }
    }

    // 创建声纹特征库
    public void creatGroup(Observer<CreatGroupRes.GroupText> observer) {
        String url = assembleRequestUrl(this.url, apiKey, apiSecret);

        CreatGroupReq req = new CreatGroupReq();
        req.header.app_id = APPID;
        req.parameter.s782b4996.groupId = groupId;
        req.parameter.s782b4996.groupName = groupId + "_groupName";
        req.parameter.s782b4996.groupInfo = groupId + "_groupInfo";

        String json = JsonUtils.toJson(req);
        LogUtils.d(json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理失败情况
                observer.onChanged(null);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        LogUtils.d(myResponse);
                        CreatGroupRes creatGroupRes = JsonUtils.toBean(myResponse, CreatGroupRes.class);
                        if (creatGroupRes.header.code != 0) {
                            LogUtils.e("失败：" + creatGroupRes.header.message);
                            observer.onChanged(null);
                        } else {
                            String text = new String(EncodeUtils.base64Decode(creatGroupRes.payload.createGroupRes.text));
                            LogUtils.d(text);
//                            {"groupId":"voice_groupId","groupName":"voice_groupName","groupInfo":"voice_groupInfo"}
                            CreatGroupRes.GroupText groupText = JsonUtils.toBean(text, CreatGroupRes.GroupText.class);
                            observer.onChanged(groupText);
                        }
                    } else {
                        LogUtils.e("code=" + response.code() + ",body=" + response.body().string());
                        observer.onChanged(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onChanged(null);
                }
            }
        });
    }

    // 添加音频特征
    public void doCreateFeature(String featureId, byte[] audio, Observer<Feature> observer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String featureInfo = featureId + "_" + sdf.format(new Date());
        doCreateFeature(featureId, featureInfo, audio, observer);
    }
    // 添加音频特征
    public void doCreateFeature(String featureId, String featureInfo, byte[] audio, Observer<Feature> observer) {
        String url = assembleRequestUrl(this.url, apiKey, apiSecret);
        CreatFeatureReq req = new CreatFeatureReq();
        req.header.app_id = APPID;
        req.parameter.s782b4996.groupId = groupId;
        req.parameter.s782b4996.featureId = featureId;
        req.parameter.s782b4996.featureInfo = featureInfo;
        req.payload.resource.audio = EncodeUtils.base64Encode2String(audio);
        req.payload.resource.encoding = "ico";

        String json = JsonUtils.toJson(req);
        LogUtils.d(json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理失败情况
                observer.onChanged(null);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        LogUtils.d(myResponse);
                        CreatFeatureRes creatGroupRes = JsonUtils.toBean(myResponse, CreatFeatureRes.class);
                        if (creatGroupRes.header.code != 0) {
                            LogUtils.e("失败：" + creatGroupRes.header.message);
                            observer.onChanged(null);
                        } else {
                            String text = new String(EncodeUtils.base64Decode(creatGroupRes.payload.createFeatureRes.text));
                            LogUtils.d(text);
                            Feature feature = JsonUtils.toBean(text, Feature.class);
                            observer.onChanged(feature);
                        }
                    } else {
                        LogUtils.e("code=" + response.code() + ",body=" + response.body().string());
                        observer.onChanged(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onChanged(null);
                }
            }
        });
    }

    // 查询特征列表
    public void doQueryFeatureList(Observer<List<Feature>> observer) {
        String url = assembleRequestUrl(this.url, apiKey, apiSecret);

        CreatGroupReq req = new CreatGroupReq();
        req.header.app_id = APPID;
        req.parameter.s782b4996.func = "queryFeatureList";
        req.parameter.s782b4996.groupId = groupId;

        String json = JsonUtils.toJson(req);
        LogUtils.d(json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理失败情况
                observer.onChanged(null);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        LogUtils.d(myResponse);
                        QueryFeatureListRes creatGroupRes = JsonUtils.toBean(myResponse, QueryFeatureListRes.class);
                        if (creatGroupRes.header.code != 0) {
                            LogUtils.e("失败：" + creatGroupRes.header.message);
                            observer.onChanged(null);
                        } else {
                            String text = new String(EncodeUtils.base64Decode(creatGroupRes.payload.queryFeatureListRes.text));
                            LogUtils.d(text);
                            List<Feature> featureList = JsonUtils.toList(text, Feature.class);
                            observer.onChanged(featureList);
                        }
                    } else {
                        LogUtils.e("code=" + response.code() + ",body=" + response.body().string());
                        observer.onChanged(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onChanged(null);
                }
            }
        });
    }

    // 特征比对1:N
    public void doSearchFeature(byte[] audio, Observer<List<Feature>> observer) {
        String url = assembleRequestUrl(this.url, apiKey, apiSecret);

        SearchFeatureReq req = new SearchFeatureReq();
        req.header.app_id = APPID;
        req.parameter.s782b4996.groupId = groupId;
        req.payload.resource.audio = EncodeUtils.base64Encode2String(audio);

        String json = JsonUtils.toJson(req);
        LogUtils.d(json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理失败情况
                observer.onChanged(null);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        LogUtils.d(myResponse);
                        SearchFeatureRes creatGroupRes = JsonUtils.toBean(myResponse, SearchFeatureRes.class);
                        if (creatGroupRes.header.code != 0) {
                            LogUtils.e("失败：" + creatGroupRes.header.message);
                            observer.onChanged(null);
                        } else {
                            String text = new String(EncodeUtils.base64Decode(creatGroupRes.payload.searchFeaRes.text));
                            LogUtils.d(text);
                            SearchFeatureRes.ResText resText = JsonUtils.toBean(text, SearchFeatureRes.ResText.class);
                            if (resText != null) {
                                observer.onChanged(resText.scoreList);
                            } else {
                                observer.onChanged(null);
                            }
                        }
                    } else {
                        LogUtils.e("code=" + response.code() + ",body=" + response.body().string());
                        observer.onChanged(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onChanged(null);
                }
            }
        });
    }

    // 删除指定特征
    public void doDeleteFeature(String featureId, Observer<Boolean> observer) {
        String url = assembleRequestUrl(this.url, apiKey, apiSecret);

        DeleteFeatureReq req = new DeleteFeatureReq();
        req.header.app_id = APPID;
        req.parameter.s782b4996.groupId = groupId;
        req.parameter.s782b4996.featureId = featureId;

        String json = JsonUtils.toJson(req);
        LogUtils.d(json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理失败情况
                observer.onChanged(null);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        LogUtils.d(myResponse);
                        DeletaFeatureRes res = JsonUtils.toBean(myResponse, DeletaFeatureRes.class);
                        if (res.header.code != 0) {
                            LogUtils.e("失败：" + res.header.message);
                            observer.onChanged(null);
                        } else {
                            String text = new String(EncodeUtils.base64Decode(res.payload.deleteFeatureRes.text));
                            LogUtils.d(text);
//                            DeletaFeatureRes.ResText resText = JsonUtils.toBean(text, DeletaFeatureRes.ResText.class);
                            if (!TextUtils.isEmpty(text)) {
                                observer.onChanged(true);
                            } else {
                                observer.onChanged(null);
                            }
                        }
                    } else {
                        LogUtils.e("code=" + response.code() + ",body=" + response.body().string());
                        observer.onChanged(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onChanged(null);
                }
            }
        });
    }

    public static byte[] read(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();
        return data;
    }

    private static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
