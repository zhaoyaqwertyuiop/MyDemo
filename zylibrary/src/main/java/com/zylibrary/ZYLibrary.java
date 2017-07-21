package com.zylibrary;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zylibrary.util.ActivityStack;
import com.zylibrary.util.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ZYLibrary {
    private static Application app;

    /**
     * 初始化
     * @param app
     * @param isLog 是否打印日志
     */
    public static void init(Application app, boolean isLog) {
        ZYLibrary.app = app;
        LogUtil.init(isLog);

        // activity 堆栈初始化
        ActivityStack.init(app);

        // okhttp 配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static Application app() {
        return app;
    }

}
